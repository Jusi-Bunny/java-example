package com.example.minio.storage;

import com.example.minio.request.PresignedUploadRequest;
import com.example.minio.response.FileUploadResponse;
import com.example.minio.response.PresignedUploadResponse;
import io.minio.GetObjectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageClient {

    /**
     * 上传文件并返回对象存储中的 object key。
     */
    FileUploadResponse upload(MultipartFile file);

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
    PresignedUploadResponse getPresignedUploadUrl(PresignedUploadRequest req);

    /**
     * 删除对象存储中的文件。
     */
    void delete(String objectName);
}
