package com.example.minio.storage.minio;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
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
    @Setter
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

}