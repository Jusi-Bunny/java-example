package com.example.minio.storage.minio;

public record UploadedPart(
        Integer partNumber,
        String etag
) {
}