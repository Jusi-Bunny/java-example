package com.example.minio.storage.multipart;

import com.example.minio.config.MinioProperties;
import com.example.minio.request.multipart.*;
import com.example.minio.response.multipart.*;
import com.example.minio.storage.minio.MultipartUploadSession;
import com.example.minio.storage.minio.MultipartUploadStatus;
import com.example.minio.storage.minio.StoredFileRecord;
import com.example.minio.storage.minio.UploadedPart;
import io.minio.*;
import io.minio.messages.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MultipartUploadService {

    private static final long MIN_PART_SIZE = 5L * 1024 * 1024;
    private static final long DEFAULT_PART_SIZE = 10L * 1024 * 1024;
    private static final int MAX_PART_COUNT = 10_000;
    private static final int URL_EXPIRE_SECONDS = 10 * 60;
    private static final String DEFAULT_UPLOAD_DIR = "multipart";

    private final MinioClient minioClient;
    private final MinioAsyncClient minioAsyncClient;
    private final MinioProperties properties;

    private final Map<String, MultipartUploadSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> activeSessionByFileKey = new ConcurrentHashMap<>();
    private final Map<String, StoredFileRecord> fileIndex = new ConcurrentHashMap<>();

    public MultipartInitResponse init(MultipartInitRequest req) {
        validateInitRequest(req);

        String fileKey = fileKey(req.fileHash(), req.fileSize());
        StoredFileRecord storedFile = fileIndex.get(fileKey);

        if (storedFile != null && objectExists(storedFile.objectName())) {
            return new MultipartInitResponse(
                    true,
                    null,
                    storedFile.objectName(),
                    null,
                    null,
                    List.of()
            );
        }

        String activeUploadId = activeSessionByFileKey.get(fileKey);
        MultipartUploadSession activeSession = activeUploadId == null ? null : sessions.get(activeUploadId);

        if (activeSession != null && activeSession.getStatus() == MultipartUploadStatus.UPLOADING) {
            return toInitResponse(false, activeSession);
        }

        ensureBucketExists();

        long partSize = choosePartSize(req.fileSize(), req.partSize());
        int partCount = calculatePartCount(req.fileSize(), partSize);
        String objectName = buildObjectName(DEFAULT_UPLOAD_DIR, req.fileName());

        try {
            CreateMultipartUploadArgs.Builder builder = CreateMultipartUploadArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName);

            if (StringUtils.hasText(req.contentType())) {
                builder.headers(new Http.Headers(Map.of("Content-Type", req.contentType())));
            }

            String uploadId = minioAsyncClient.createMultipartUpload(builder.build())
                    .join()
                    .result()
                    .uploadId();

            MultipartUploadSession session = new MultipartUploadSession(
                    uploadId,
                    objectName,
                    req.fileName(),
                    req.contentType(),
                    req.fileSize(),
                    req.fileHash(),
                    partSize,
                    partCount
            );

            sessions.put(uploadId, session);
            activeSessionByFileKey.put(fileKey, uploadId);

            return toInitResponse(false, session);
        } catch (Exception e) {
            throw new RuntimeException("初始化分片上传失败", e);
        }
    }

    public MultipartPartUrlResponse getPartUrl(MultipartPartUrlRequest req) {
        MultipartUploadSession session = requireUploadingSession(req.uploadId());

        if (!session.getObjectName().equals(req.objectName())) {
            throw new IllegalArgumentException("objectName 与上传会话不匹配");
        }
        if (req.partNumber() == null || req.partNumber() < 1 || req.partNumber() > session.getPartCount()) {
            throw new IllegalArgumentException("partNumber 不合法");
        }

        try {
            String uploadUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Http.Method.PUT)
                            .bucket(properties.getBucket())
                            .object(session.getObjectName())
                            .extraQueryParams(Map.of(
                                    "partNumber", String.valueOf(req.partNumber()),
                                    "uploadId", session.getUploadId()
                            ))
                            .expiry(URL_EXPIRE_SECONDS, TimeUnit.SECONDS)
                            .build()
            );

            return new MultipartPartUrlResponse(req.partNumber(), uploadUrl, URL_EXPIRE_SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("生成 Part 上传 URL 失败", e);
        }
    }

    public void completePart(MultipartPartCompleteRequest req) {
        MultipartUploadSession session = requireUploadingSession(req.uploadId());

        if (req.partNumber() == null || req.partNumber() < 1 || req.partNumber() > session.getPartCount()) {
            throw new IllegalArgumentException("partNumber 不合法");
        }
        if (!StringUtils.hasText(req.etag())) {
            throw new IllegalArgumentException("ETag 不能为空");
        }

        String etag = normalizeEtag(req.etag());
        session.getUploadedParts().put(req.partNumber(), new UploadedPart(req.partNumber(), etag));
    }

    public MultipartCompleteResponse complete(MultipartCompleteRequest req) {
        MultipartUploadSession session = requireUploadingSession(req.uploadId());

        if (!session.getObjectName().equals(req.objectName())) {
            throw new IllegalArgumentException("objectName 与上传会话不匹配");
        }
        if (session.getUploadedParts().size() != session.getPartCount()) {
            throw new IllegalStateException("分片未全部上传完成");
        }

        Part[] parts = session.getUploadedParts()
                .values()
                .stream()
                .sorted(Comparator.comparing(UploadedPart::partNumber))
                .map(part -> new Part(part.partNumber(), part.etag()))
                .toArray(Part[]::new);

        try {
            minioAsyncClient.completeMultipartUpload(
                    CompleteMultipartUploadArgs.builder()
                            .bucket(properties.getBucket())
                            .object(session.getObjectName())
                            .uploadId(session.getUploadId())
                            .parts(parts)
                            .build()
            ).join();

            session.setStatus(MultipartUploadStatus.COMPLETED);
            activeSessionByFileKey.remove(fileKey(session.getFileHash(), session.getFileSize()));

            StoredFileRecord storedFile = new StoredFileRecord(
                    session.getObjectName(),
                    session.getOriginalName(),
                    session.getContentType(),
                    session.getFileSize(),
                    session.getFileHash()
            );
            fileIndex.put(fileKey(session.getFileHash(), session.getFileSize()), storedFile);

            return new MultipartCompleteResponse(
                    storedFile.objectName(),
                    storedFile.originalName(),
                    storedFile.contentType(),
                    storedFile.size(),
                    storedFile.fileHash()
            );
        } catch (Exception e) {
            throw new RuntimeException("完成分片上传失败", e);
        }
    }

    public MultipartStatusResponse status(String uploadId) {
        MultipartUploadSession session = sessions.get(uploadId);
        if (session == null) {
            throw new IllegalArgumentException("上传会话不存在");
        }

        return new MultipartStatusResponse(
                session.getUploadId(),
                session.getObjectName(),
                session.getStatus().name(),
                session.getPartSize(),
                session.getPartCount(),
                toUploadedPartResponses(session)
        );
    }

    public void abort(MultipartAbortRequest req) {
        MultipartUploadSession session = requireUploadingSession(req.uploadId());

        if (!session.getObjectName().equals(req.objectName())) {
            throw new IllegalArgumentException("objectName 与上传会话不匹配");
        }

        try {
            minioAsyncClient.abortMultipartUpload(
                    AbortMultipartUploadArgs.builder()
                            .bucket(properties.getBucket())
                            .object(session.getObjectName())
                            .uploadId(session.getUploadId())
                            .build()
            ).join();

            session.setStatus(MultipartUploadStatus.ABORTED);
            activeSessionByFileKey.remove(fileKey(session.getFileHash(), session.getFileSize()));
        } catch (Exception e) {
            throw new RuntimeException("取消分片上传失败", e);
        }
    }

    private void validateInitRequest(MultipartInitRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("请求不能为空");
        }
        if (!StringUtils.hasText(req.fileName())) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        if (req.fileSize() == null || req.fileSize() <= 0) {
            throw new IllegalArgumentException("文件大小不合法");
        }
        if (!StringUtils.hasText(req.fileHash())) {
            throw new IllegalArgumentException("文件摘要不能为空");
        }
    }

    private MultipartUploadSession requireUploadingSession(String uploadId) {
        if (!StringUtils.hasText(uploadId)) {
            throw new IllegalArgumentException("uploadId 不能为空");
        }

        MultipartUploadSession session = sessions.get(uploadId);
        if (session == null) {
            throw new IllegalArgumentException("上传会话不存在");
        }
        if (session.getStatus() != MultipartUploadStatus.UPLOADING) {
            throw new IllegalStateException("上传会话不是上传中状态");
        }

        return session;
    }

    private MultipartInitResponse toInitResponse(boolean instant, MultipartUploadSession session) {
        return new MultipartInitResponse(
                instant,
                session.getUploadId(),
                session.getObjectName(),
                session.getPartSize(),
                session.getPartCount(),
                toUploadedPartResponses(session)
        );
    }

    private List<UploadedPartResponse> toUploadedPartResponses(MultipartUploadSession session) {
        return session.getUploadedParts()
                .values()
                .stream()
                .sorted(Comparator.comparing(UploadedPart::partNumber))
                .map(part -> new UploadedPartResponse(part.partNumber(), part.etag()))
                .toList();
    }

    private long choosePartSize(long fileSize, Long requestedPartSize) {
        long partSize = requestedPartSize == null ? DEFAULT_PART_SIZE : requestedPartSize;
        partSize = Math.max(partSize, MIN_PART_SIZE);

        long minPartSizeByCount = (long) Math.ceil(fileSize / (double) MAX_PART_COUNT);
        partSize = Math.max(partSize, minPartSizeByCount);

        return partSize;
    }

    private int calculatePartCount(long fileSize, long partSize) {
        long count = (long) Math.ceil(fileSize / (double) partSize);
        if (count > MAX_PART_COUNT) {
            throw new IllegalArgumentException("分片数量超过 10000，请增大 partSize");
        }
        return (int) count;
    }

    private boolean objectExists(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(properties.getBucket())
                            .build()
            );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(properties.getBucket())
                                .build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("检查或创建 bucket 失败", e);
        }
    }

    private String buildObjectName(String bizDir, String originalFilename) {
        LocalDate now = LocalDate.now();
        String ext = StringUtils.getFilenameExtension(originalFilename);
        String filename = UUID.randomUUID().toString().replace("-", "");

        if (StringUtils.hasText(ext)) {
            filename = filename + "." + ext;
        }

        return bizDir + "/"
                + now.getYear() + "/"
                + String.format("%02d", now.getMonthValue()) + "/"
                + String.format("%02d", now.getDayOfMonth()) + "/"
                + filename;
    }

    private String fileKey(String fileHash, Long fileSize) {
        return fileHash + ":" + fileSize;
    }

    private String normalizeEtag(String etag) {
        return etag.trim().replace("\"", "");
    }
}