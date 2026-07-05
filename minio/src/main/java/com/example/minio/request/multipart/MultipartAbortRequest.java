package com.example.minio.request.multipart;

public record MultipartAbortRequest(
        String uploadId,
        String objectName
) {
}