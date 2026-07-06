package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartPartCompleteRequest {

    // 定位上传会话
    private String uploadId;

    // 标识哪个分片已完成
    private Integer partNumber;

    // MinIO 返回的分片标识，最终合并必须提交
    private String etag;
}