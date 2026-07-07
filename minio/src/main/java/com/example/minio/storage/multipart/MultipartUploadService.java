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

        String fileKey = fileKey(req.getFileHash(), req.getFileSize());
        StoredFileRecord storedFile = fileIndex.get(fileKey);

        if (storedFile != null && objectExists(storedFile.getObjectName())) {
            return new MultipartInitResponse(
                    true,
                    null,
                    storedFile.getObjectName(),
                    null,
                    null,
                    List.of()
            );
        }

        String activeUploadId = activeSessionByFileKey.get(fileKey);
        MultipartUploadSession activeSession = activeUploadId == null ? null : sessions.get(activeUploadId);

        if (activeSession != null && isActiveSession(activeSession)) {
            return toInitResponse(false, activeSession);
        }

        ensureBucketExists();

        long partSize = choosePartSize(req.getFileSize(), req.getPartSize());
        int partCount = calculatePartCount(req.getFileSize(), partSize);
        String objectName = buildObjectName(DEFAULT_UPLOAD_DIR, req.getFileName());

        try {
            CreateMultipartUploadArgs.Builder builder = CreateMultipartUploadArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName);

            if (StringUtils.hasText(req.getContentType())) {
                builder.headers(new Http.Headers(Map.of("Content-Type", req.getContentType())));
            }

            String uploadId = minioAsyncClient.createMultipartUpload(builder.build())
                    .join()
                    .result()
                    .uploadId();

            MultipartUploadSession session = new MultipartUploadSession(
                    uploadId,
                    objectName,
                    req.getFileName(),
                    req.getContentType(),
                    req.getFileSize(),
                    req.getFileHash(),
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
        MultipartUploadSession session = requireSessionWithStatus(req.getUploadId(), MultipartUploadStatus.UPLOADING);

        validateObjectName(session, req.getObjectName());
        if (req.getPartNumber() == null || req.getPartNumber() < 1 || req.getPartNumber() > session.getPartCount()) {
            throw new IllegalArgumentException("partNumber 不合法");
        }

        try {
            String uploadUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Http.Method.PUT)
                            .bucket(properties.getBucket())
                            .object(session.getObjectName())
                            .extraQueryParams(Map.of(
                                    "partNumber", String.valueOf(req.getPartNumber()),
                                    "uploadId", session.getUploadId()
                            ))
                            .expiry(URL_EXPIRE_SECONDS, TimeUnit.SECONDS)
                            .build()
            );

            return new MultipartPartUrlResponse(req.getPartNumber(), uploadUrl, URL_EXPIRE_SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("生成 Part 上传 URL 失败", e);
        }
    }

    public void completePart(MultipartPartCompleteRequest req) {
        MultipartUploadSession session = requirePartCompletionSession(req.getUploadId());

        if (req.getPartNumber() == null || req.getPartNumber() < 1 || req.getPartNumber() > session.getPartCount()) {
            throw new IllegalArgumentException("partNumber 不合法");
        }
        if (!StringUtils.hasText(req.getEtag())) {
            throw new IllegalArgumentException("ETag 不能为空");
        }

        String etag = normalizeEtag(req.getEtag());
        session.getUploadedParts().put(req.getPartNumber(), new UploadedPart(req.getPartNumber(), etag));
    }

    public MultipartStatusResponse pause(MultipartPauseRequest req) {
        MultipartUploadSession session = requireSessionWithStatus(req.getUploadId(), MultipartUploadStatus.UPLOADING);
        validateObjectName(session, req.getObjectName());

        session.setStatus(MultipartUploadStatus.PAUSED);
        return toStatusResponse(session);
    }

    public MultipartStatusResponse resume(MultipartResumeRequest req) {
        MultipartUploadSession session = requireSessionWithStatus(req.getUploadId(), MultipartUploadStatus.PAUSED);
        validateObjectName(session, req.getObjectName());

        session.setStatus(MultipartUploadStatus.UPLOADING);
        return toStatusResponse(session);
    }

    public MultipartCompleteResponse complete(MultipartCompleteRequest req) {
        MultipartUploadSession session = requireSessionWithStatus(req.getUploadId(), MultipartUploadStatus.UPLOADING);

        validateObjectName(session, req.getObjectName());
        if (session.getUploadedParts().size() != session.getPartCount()) {
            throw new IllegalStateException("分片未全部上传完成");
        }

        Part[] parts = session.getUploadedParts()
                .values()
                .stream()
                .sorted(Comparator.comparing(UploadedPart::getPartNumber))
                .map(part -> new Part(part.getPartNumber(), part.getEtag()))
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
                    storedFile.getObjectName(),
                    storedFile.getOriginalName(),
                    storedFile.getContentType(),
                    storedFile.getSize(),
                    storedFile.getFileHash()
            );
        } catch (Exception e) {
            throw new RuntimeException("完成分片上传失败", e);
        }
    }

    public MultipartStatusResponse status(String uploadId) {
        return toStatusResponse(requireSession(uploadId));
    }

    public void abort(MultipartAbortRequest req) {
        MultipartUploadSession session = requireSessionWithStatus(
                req.getUploadId(),
                MultipartUploadStatus.UPLOADING,
                MultipartUploadStatus.PAUSED
        );

        validateObjectName(session, req.getObjectName());

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
        if (!StringUtils.hasText(req.getFileName())) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        if (req.getFileSize() == null || req.getFileSize() <= 0) {
            throw new IllegalArgumentException("文件大小不合法");
        }
        if (!StringUtils.hasText(req.getFileHash())) {
            throw new IllegalArgumentException("文件摘要不能为空");
        }
    }

    private MultipartUploadSession requireSession(String uploadId) {
        if (!StringUtils.hasText(uploadId)) {
            throw new IllegalArgumentException("uploadId 不能为空");
        }

        MultipartUploadSession session = sessions.get(uploadId);
        if (session == null) {
            throw new IllegalArgumentException("上传会话不存在");
        }

        return session;
    }

    private MultipartUploadSession requireSessionWithStatus(String uploadId, MultipartUploadStatus... allowedStatuses) {
        MultipartUploadSession session = requireSession(uploadId);

        for (MultipartUploadStatus allowedStatus : allowedStatuses) {
            if (session.getStatus() == allowedStatus) {
                return session;
            }
        }

        throw new IllegalStateException("上传会话当前状态不允许此操作，当前状态：" + session.getStatus().name());
    }

    private MultipartUploadSession requirePartCompletionSession(String uploadId) {
        MultipartUploadSession session = requireSession(uploadId);
        if (session.getStatus() == MultipartUploadStatus.ABORTED
                || session.getStatus() == MultipartUploadStatus.COMPLETED) {
            throw new IllegalStateException("上传会话当前状态不允许登记分片完成，当前状态：" + session.getStatus().name());
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

    private MultipartStatusResponse toStatusResponse(MultipartUploadSession session) {
        return new MultipartStatusResponse(
                session.getUploadId(),
                session.getObjectName(),
                session.getOriginalName(),
                session.getFileSize(),
                session.getStatus().name(),
                session.getPartSize(),
                session.getPartCount(),
                toUploadedPartResponses(session)
        );
    }

    private List<UploadedPartResponse> toUploadedPartResponses(MultipartUploadSession session) {
        return session.getUploadedParts()
                .values()
                .stream()
                .sorted(Comparator.comparing(UploadedPart::getPartNumber))
                .map(part -> new UploadedPartResponse(part.getPartNumber(), part.getEtag()))
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

    private boolean isActiveSession(MultipartUploadSession session) {
        return session.getStatus() == MultipartUploadStatus.UPLOADING
                || session.getStatus() == MultipartUploadStatus.PAUSED;
    }

    private void validateObjectName(MultipartUploadSession session, String objectName) {
        if (!session.getObjectName().equals(objectName)) {
            throw new IllegalArgumentException("objectName 与上传会话不匹配");
        }
    }

    private String normalizeEtag(String etag) {
        return etag.trim().replace("\"", "");
    }
}
