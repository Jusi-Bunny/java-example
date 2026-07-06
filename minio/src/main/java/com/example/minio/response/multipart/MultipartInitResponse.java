package com.example.minio.response.multipart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MultipartInitResponse {

    // 是否秒传命中；为 `true` 时前端不再上传
    private Boolean instant;

    // 本次 multipart 会话 ID
    private String uploadId;

    // 最终对象名
    private String objectName;

    // 后端确认后的分片大小
    private Long partSize;

    // 总分片数
    private Integer partCount;

    // 已上传分片列表，用于断点续传
    private List<UploadedPartResponse> uploadedParts;
}
