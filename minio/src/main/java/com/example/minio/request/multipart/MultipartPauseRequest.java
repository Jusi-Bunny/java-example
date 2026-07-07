package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartPauseRequest {
    private String uploadId;
    private String objectName;
}
