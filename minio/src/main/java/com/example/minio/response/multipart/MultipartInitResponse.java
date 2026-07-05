package com.example.minio.response.multipart;

import java.util.List;

/**
 * MinIO 初始化响应
 * @param instant
 * @param uploadId
 * @param objectName
 * @param partSize
 * @param partCount
 * @param uploadedParts
 */
public record MultipartInitResponse(
        Boolean instant,
        String uploadId,
        String objectName,
        Long partSize,
        Integer partCount,
        List<UploadedPartResponse> uploadedParts
) {
}