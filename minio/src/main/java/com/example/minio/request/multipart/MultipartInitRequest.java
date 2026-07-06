package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartInitRequest {

    // 保留原始文件名，也用于生成带扩展名的 `objectName`
    private String fileName;

    // 计算分片数量，并和 `fileHash` 一起做秒传 key
    private Long fileSize;

    // 文件内容摘要，是秒传和断点续传匹配的核心
    private String fileHash;

    // 设置最终对象的 MIME 类型
    private String contentType;

    // 前端期望分片大小；后端会校正为合法值
    private Long partSize;
}