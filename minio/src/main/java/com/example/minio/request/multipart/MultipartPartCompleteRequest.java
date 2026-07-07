package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartPartCompleteRequest {
    private String uploadId;
    private Integer partNumber;
    private String etag;
}
