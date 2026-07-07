package com.example.deploylab.controller;

import com.example.deploylab.common.ApiResponse;
import com.example.deploylab.model.RequestRecord;
import com.example.deploylab.service.RequestRecordService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/request-records")
public class RequestRecordController {

    private final RequestRecordService requestRecordService;

    public RequestRecordController(RequestRecordService requestRecordService) {
        this.requestRecordService = requestRecordService;
    }

    @GetMapping
    public ApiResponse<List<RequestRecord>> list(@RequestParam(defaultValue = "100") @Min(1) @Max(100) int limit) {
        return ApiResponse.success(requestRecordService.list(limit));
    }

    @DeleteMapping
    public ApiResponse<Void> deleteAll() {
        requestRecordService.deleteAll();
        return ApiResponse.success(null);
    }
}
