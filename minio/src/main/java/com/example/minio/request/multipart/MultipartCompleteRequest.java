package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartCompleteRequest {

    // 定位要完成的 multipart 会话
    private String uploadId;

    // 校验本次完成操作对应的目标对象
    private String objectName;
}