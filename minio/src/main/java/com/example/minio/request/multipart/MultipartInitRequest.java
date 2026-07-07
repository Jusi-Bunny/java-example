package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartInitRequest {
    private String fileName;
    private Long fileSize;
    private String fileHash;
    private String contentType;
    private Long partSize;
}
