package com.example.minio.response.multipart;

public record UploadedPartResponse(
        Integer partNumber,
        String etag
) {
}