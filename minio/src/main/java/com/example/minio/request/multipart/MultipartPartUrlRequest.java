package com.example.minio.request.multipart;

public record MultipartPartUrlRequest(
        String uploadId,
        String objectName,
        Integer partNumber
) {
}