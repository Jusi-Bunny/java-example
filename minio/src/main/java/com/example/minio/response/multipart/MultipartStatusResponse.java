package com.example.minio.response.multipart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MultipartStatusResponse {

    private String uploadId;

    private String objectName;

    private String status;

    private Long partSize;

    private Integer partCount;

    private List<UploadedPartResponse> uploadedParts;
}
