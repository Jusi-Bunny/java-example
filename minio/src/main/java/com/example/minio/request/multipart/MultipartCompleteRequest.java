package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartCompleteRequest {
    private String uploadId;
    private String objectName;
}
