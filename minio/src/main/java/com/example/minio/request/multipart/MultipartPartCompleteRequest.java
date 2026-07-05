package com.example.minio.request.multipart;

public record MultipartPartCompleteRequest(
        String uploadId,
        Integer partNumber,
        String etag
) {
}