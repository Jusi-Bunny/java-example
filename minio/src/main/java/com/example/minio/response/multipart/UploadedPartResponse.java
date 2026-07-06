package com.example.minio.response.multipart;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadedPartResponse {

    private Integer partNumber;

    private String etag;
}
