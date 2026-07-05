package com.example.minio.response.multipart;

public record MultipartPartUrlResponse(
        Integer partNumber,
        String uploadUrl,
        Integer expireSeconds
) {
}