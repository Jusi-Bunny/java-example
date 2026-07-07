package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartResumeRequest {
    private String uploadId;
    private String objectName;
}
