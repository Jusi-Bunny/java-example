package com.example.minio.request.multipart;

public record MultipartInitRequest(
        String fileName,
        Long fileSize,
        String fileHash,
        String contentType,
        Long partSize
) {
}