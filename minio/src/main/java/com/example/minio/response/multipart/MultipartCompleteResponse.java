package com.example.minio.response.multipart;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MultipartCompleteResponse {

    private String objectName;

    private String originalName;

    private String contentType;

    private Long size;

    private String fileHash;
}