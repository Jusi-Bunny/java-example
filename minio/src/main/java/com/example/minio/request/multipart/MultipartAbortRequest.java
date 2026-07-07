package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartAbortRequest {
    String uploadId;
    String objectName;
}
