package com.example.minio.response;

import lombok.Data;

@Data
public class FileUploadResponse {

    private String objectName;
    private String originalName;
    private String contentType;
    private Long size;
}
