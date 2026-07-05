package com.example.minio.request.multipart;

public record MultipartCompleteRequest(
        String uploadId,
        String objectName
) {
}