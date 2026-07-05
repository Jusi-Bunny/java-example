package com.example.minio.response.multipart;

public record MultipartCompleteResponse(
        String objectName,
        String originalName,
        String contentType,
        Long size,
        String fileHash
) {
}