package com.example.minio.storage.minio;

import com.example.minio.config.MinioProperties;
import com.example.minio.request.PresignedUploadRequest;
import com.example.minio.response.FileUploadResponse;
import com.example.minio.response.PresignedUploadResponse;
import com.example.minio.storage.ObjectStorageClient;
import com.example.minio.storage.ObjectStorageException;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "storage", name = "type", havingValue = "minio", matchIfMissing = true)
public class MinioObjectStorageClient implements ObjectStorageClient {

    private final MinioClient minioClient;

    private final MinioProperties properties;

    private final String DEFAULT_UPLOAD_DIR = "upload";

    @Override
    public FileUploadResponse upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ObjectStorageException("上传文件不能为空");
        }

        FileUploadResponse response = new FileUploadResponse();

        try {
            ensureBucketExists();

            String originalFilename = file.getOriginalFilename();
            String ext = StringUtils.getFilenameExtension(originalFilename);

            String objectName = buildObjectName(DEFAULT_UPLOAD_DIR, ext);

            String contentType = file.getContentType();
            if (!StringUtils.hasText(contentType)) {
                contentType = "application/octet-stream";
            }

            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(properties.getBucket())
                                .object(objectName)
                                .stream(inputStream, file.getSize(), -1L)
                                .contentType(contentType)
                                .build()
                );
            }
            response.setObjectName(objectName);
            response.setOriginalName(file.getOriginalFilename());
            response.setContentType(file.getContentType());
            response.setSize(file.getSize());
            return response;
        } catch (Exception e) {
            throw new ObjectStorageException("上传文件到 MinIO 失败", e);
        }
    }

    @Override
    public GetObjectResponse getObject(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new ObjectStorageException("从 MinIO 下载文件失败", e);
        }
    }

    @Override
    public String getPresignedDownloadUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Http.Method.GET)
                            .bucket(properties.getBucket())
                            .object(objectName)
                            .expiry(10, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            throw new ObjectStorageException("生成预签名下载 URL 失败", e);
        }
    }

    @Override
    public PresignedUploadResponse getPresignedUploadUrl(PresignedUploadRequest req) {
        PresignedUploadResponse response = new PresignedUploadResponse();
        try {

            String ext = StringUtils.getFilenameExtension(req.getFileName());
            String objectName = buildObjectName(DEFAULT_UPLOAD_DIR, ext);
            String uploadUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Http.Method.PUT)
                            .bucket(properties.getBucket())
                            .object(objectName)
                            .expiry(10, TimeUnit.MINUTES)
                            .build()
            );
            response.setUploadUrl(uploadUrl);
            response.setObjectName(objectName);
            response.setExpireSeconds(10 * 60);
            return response;
        } catch (Exception e) {
            throw new ObjectStorageException("生成预签名上传 URL 失败", e);
        }
    }

    @Override
    public void delete(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new ObjectStorageException("删除 MinIO 文件失败", e);
        }
    }

    /**
     * 确保 Bucket 存在，如果不存在则创建
     *
     */
    private void ensureBucketExists() throws Exception {
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
    }

    /**
     * 构建对象名称，格式为：业务目录/年/月/日/uuid.ext
     *
     * @param bizDir 业务目录
     * @param ext    文件扩展名
     * @return 对象名称
     */
    private String buildObjectName(String bizDir, String ext) {
        LocalDate now = LocalDate.now();

        String filename = UUID.randomUUID().toString().replace("-", "");

        if (StringUtils.hasText(ext)) {
            filename = filename + "." + ext;
        }

        return normalizeBizDir(bizDir) + "/"
                + now.getYear() + "/"
                + String.format("%02d", now.getMonthValue()) + "/"
                + String.format("%02d", now.getDayOfMonth()) + "/"
                + filename;
    }

    /**
     * 格式化业务目录，删除多余的斜杠，并添加默认值 "default"
     *
     * @param bizDir 业务目录
     * @return 格式化后的业务目录
     */
    private String normalizeBizDir(String bizDir) {
        if (!StringUtils.hasText(bizDir)) {
            return "default";
        }

        String normalized = bizDir.trim().replace("\\", "/");
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        return StringUtils.hasText(normalized) ? normalized : "default";
    }
}
