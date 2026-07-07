package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartPartUrlRequest {
    private String uploadId;
    private String objectName;
    private Integer partNumber;
}
