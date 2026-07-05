package com.example.minio.response.multipart;

import java.util.List;

public record MultipartStatusResponse(
        String uploadId,
        String objectName,
        String status,
        Long partSize,
        Integer partCount,
        List<UploadedPartResponse> uploadedParts
) {
}