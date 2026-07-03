package com.example.minio.controller;

import com.example.minio.common.result.Result;
import com.example.minio.request.PresignedUploadRequest;
import com.example.minio.response.FileUploadResponse;
import com.example.minio.response.PresignedUploadResponse;
import com.example.minio.storage.ObjectStorageClient;
import io.minio.GetObjectResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final ObjectStorageClient objectStorageClient;

    @PostMapping("/upload")
    public Result<FileUploadResponse> upload(@RequestParam("file") MultipartFile file) {
        return Result.ok(objectStorageClient.upload(file));
    }

    @GetMapping("/download")
    public Result<Void> download(@RequestParam String objectName,
                                 HttpServletResponse response) {
        try (GetObjectResponse inputStream = objectStorageClient.getObject(objectName)) {
            response.setContentType("application/octet-stream");

            inputStream.transferTo(response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("下载文件失败", e);
        }
        return Result.ok();
    }

    @GetMapping("/download-url")
    public Result<String> getDownloadUrl(@RequestParam String objectName) {
        return Result.ok(objectStorageClient.getPresignedDownloadUrl(objectName));
    }

    @PostMapping("/presigned-upload-url")
    public Result<PresignedUploadResponse> getUploadUrl(@RequestBody PresignedUploadRequest req) {
        return Result.ok(objectStorageClient.getPresignedUploadUrl(req));
    }

    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam String objectName) {
        objectStorageClient.delete(objectName);
        return Result.ok("删除成功");
    }
}