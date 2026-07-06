package com.example.minio.storage.minio;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadedPart {

    private Integer partNumber;

    private String etag;
}
