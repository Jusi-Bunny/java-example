package com.example.minio.storage.minio;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultipartUploadSession {

    private final String uploadId;
    private final String objectName;
    private final String originalName;
    private final String contentType;
    private final Long fileSize;
    private final String fileHash;
    private final Long partSize;
    private final Integer partCount;
    private final Map<Integer, UploadedPart> uploadedParts = new ConcurrentHashMap<>();
    private volatile MultipartUploadStatus status = MultipartUploadStatus.UPLOADING;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public MultipartUploadSession(String uploadId,
                                  String objectName,
                                  String originalName,
                                  String contentType,
                                  Long fileSize,
                                  String fileHash,
                                  Long partSize,
                                  Integer partCount) {
        this.uploadId = uploadId;
        this.objectName = objectName;
        this.originalName = originalName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.fileHash = fileHash;
        this.partSize = partSize;
        this.partCount = partCount;
    }

    public String getUploadId() {
        return uploadId;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getFileHash() {
        return fileHash;
    }

    public Long getPartSize() {
        return partSize;
    }

    public Integer getPartCount() {
        return partCount;
    }

    public Map<Integer, UploadedPart> getUploadedParts() {
        return uploadedParts;
    }

    public MultipartUploadStatus getStatus() {
        return status;
    }

    public void setStatus(MultipartUploadStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}