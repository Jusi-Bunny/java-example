package com.example.minio.response;

import lombok.Data;

@Data
public class PresignedUploadResponse {

    String objectName;

    String uploadUrl;

    Integer expireSeconds;
}
