package com.example.minio.client;

import io.minio.GetObjectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageClient {

    /**
     * 上传文件并返回对象存储中的 object key。
     */
    String upload(MultipartFile file, String bizDir);


    /**
     * 获取对象存储中的文件。
     */
    GetObjectResponse getObject(String objectName);

    /**
     * 获取对象存储中的文件下载地址。
     */
    String getPresignedDownloadUrl(String objectName);

    /**
     * 获取对象存储中的文件上传签名 PUT URL
     */
    String getPresignedUploadUrl(String objectName);
}
