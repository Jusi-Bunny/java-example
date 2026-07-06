package com.example.minio.storage.minio;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoredFileRecord {

    private String objectName;

    private String originalName;

    private String contentType;

    private Long size;

    private String fileHash;
}
