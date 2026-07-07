package com.example.deploylab.controller;

import com.example.deploylab.common.ApiResponse;
import com.example.deploylab.dto.response.SystemInfoResponse;
import com.example.deploylab.service.SystemInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    private final SystemInfoService systemInfoService;

    public SystemController(SystemInfoService systemInfoService) {
        this.systemInfoService = systemInfoService;
    }

    @GetMapping("/info")
    public ApiResponse<SystemInfoResponse> info() {
        return ApiResponse.success(systemInfoService.getInfo());
    }
}
