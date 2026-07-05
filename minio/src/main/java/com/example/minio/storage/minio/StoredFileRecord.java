package com.example.minio.storage.minio;

public record StoredFileRecord(
        String objectName,
        String originalName,
        String contentType,
        Long size,
        String fileHash
) {
}