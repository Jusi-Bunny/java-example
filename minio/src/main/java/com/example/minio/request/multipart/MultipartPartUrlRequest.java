package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartPartUrlRequest {

    // 定位 MinIO multipart 会话
    private String uploadId;

    // 防止前端拿 A 会话请求 B 对象的 URL
    private String objectName;

    // 告诉后端要为第几个分片签名，编号从 1 开始
    private Integer partNumber;
}