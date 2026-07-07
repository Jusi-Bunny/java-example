package com.example.minio.response.multipart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MultipartInitResponse {
    private Boolean instant;
    private String uploadId;
    private String objectName;
    private Long partSize;
    private Integer partCount;
    private List<UploadedPartResponse> uploadedParts;
}
