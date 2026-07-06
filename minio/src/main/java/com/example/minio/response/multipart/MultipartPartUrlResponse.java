package com.example.minio.response.multipart;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MultipartPartUrlResponse {

    private Integer partNumber;

    private String uploadUrl;

    private Integer expireSeconds;
}
