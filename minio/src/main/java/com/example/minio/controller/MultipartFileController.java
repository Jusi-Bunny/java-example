package com.example.minio.controller;

import com.example.minio.common.result.Result;
import com.example.minio.request.multipart.MultipartAbortRequest;
import com.example.minio.request.multipart.MultipartCompleteRequest;
import com.example.minio.request.multipart.MultipartInitRequest;
import com.example.minio.request.multipart.MultipartPartCompleteRequest;
import com.example.minio.request.multipart.MultipartPartUrlRequest;
import com.example.minio.response.multipart.MultipartCompleteResponse;
import com.example.minio.response.multipart.MultipartInitResponse;
import com.example.minio.response.multipart.MultipartPartUrlResponse;
import com.example.minio.response.multipart.MultipartStatusResponse;
import com.example.minio.storage.multipart.MultipartUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files/multipart")
@RequiredArgsConstructor
public class MultipartFileController {

    private final MultipartUploadService multipartUploadService;

    @PostMapping("/init")
    public Result<MultipartInitResponse> init(@RequestBody MultipartInitRequest req) {
        return Result.ok(multipartUploadService.init(req));
    }

    @PostMapping("/part-url")
    public Result<MultipartPartUrlResponse> getPartUrl(@RequestBody MultipartPartUrlRequest req) {
        return Result.ok(multipartUploadService.getPartUrl(req));
    }

    @PostMapping("/part-complete")
    public Result<Void> completePart(@RequestBody MultipartPartCompleteRequest req) {
        multipartUploadService.completePart(req);
        return Result.ok();
    }

    @PostMapping("/complete")
    public Result<MultipartCompleteResponse> complete(@RequestBody MultipartCompleteRequest req) {
        return Result.ok(multipartUploadService.complete(req));
    }

    @GetMapping("/status")
    public Result<MultipartStatusResponse> status(@RequestParam String uploadId) {
        return Result.ok(multipartUploadService.status(uploadId));
    }

    @PostMapping("/abort")
    public Result<Void> abort(@RequestBody MultipartAbortRequest req) {
        multipartUploadService.abort(req);
        return Result.ok();
    }
}