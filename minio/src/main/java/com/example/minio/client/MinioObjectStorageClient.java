package com.example.minio.client;

import com.example.minio.config.MinioProperties;
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

    @Override
    public String upload(MultipartFile file, String bizDir) {
        if (file == null || file.isEmpty()) {
            throw new ObjectStorageException("上传文件不能为空");
        }

        try {
            ensureBucketExists();

            String originalFilename = file.getOriginalFilename();
            String ext = StringUtils.getFilenameExtension(originalFilename);

            String objectName = buildObjectName(bizDir, ext);

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

            return objectName;
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
            throw new RuntimeException("从 MinIO 下载文件失败", e);
        }
    }

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
            throw new RuntimeException("生成预签名下载 URL 失败", e);
        }
    }

    @Override
    public String getPresignedUploadUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Http.Method.PUT)
                            .bucket(properties.getBucket())
                            .object(objectName)
                            .expiry(10, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("生成预签名上传 URL 失败", e);
        }
    }

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
