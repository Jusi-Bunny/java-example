package com.example.minio.request.multipart;

import lombok.Data;

@Data
public class MultipartAbortRequest {

    // 定位要取消的 multipart 会话
    String uploadId;

    // 校验取消操作对应的目标对象
    String objectName;
}