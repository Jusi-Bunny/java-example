package com.example.minio.storage.minio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@RequiredArgsConstructor
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
    private final LocalDateTime createdAt = LocalDateTime.now();
    @Setter
    private volatile MultipartUploadStatus status = MultipartUploadStatus.UPLOADING;
}
