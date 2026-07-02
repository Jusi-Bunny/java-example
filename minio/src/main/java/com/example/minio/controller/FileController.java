package com.example.minio.controller;

import com.example.minio.client.ObjectStorageClient;
import io.minio.GetObjectResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final ObjectStorageClient objectStorageClient;

    @PostMapping("/upload")
    public FileUploadVO upload(@RequestParam("file") MultipartFile file) {
        String objectName = objectStorageClient.upload(file, "upload");

        FileUploadVO vo = new FileUploadVO();
        vo.setObjectName(objectName);
        vo.setOriginalName(file.getOriginalFilename());
        vo.setContentType(file.getContentType());
        vo.setSize(file.getSize());

        return vo;
    }

    @Data
    public static class FileUploadVO {
        private String objectName;
        private String originalName;
        private String contentType;
        private Long size;
    }

    @GetMapping("/download")
    public void download(@RequestParam String objectName,
                         HttpServletResponse response) {
        try (GetObjectResponse inputStream = objectStorageClient.getObject(objectName)) {
            response.setContentType("application/octet-stream");

            inputStream.transferTo(response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("下载文件失败", e);
        }
    }

    @GetMapping("/download-url")
    public String getDownloadUrl(@RequestParam String objectName) {
        return objectStorageClient.getPresignedDownloadUrl(objectName);
    }
}