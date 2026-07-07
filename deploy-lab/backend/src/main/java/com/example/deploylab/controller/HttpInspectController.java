package com.example.deploylab.controller;

import com.example.deploylab.common.ApiResponse;
import com.example.deploylab.dto.response.HttpInspectResponse;
import com.example.deploylab.service.HttpInspectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/http/inspect")
public class HttpInspectController {

    private final HttpInspectService httpInspectService;

    public HttpInspectController(HttpInspectService httpInspectService) {
        this.httpInspectService = httpInspectService;
    }

    @GetMapping
    public ApiResponse<HttpInspectResponse> inspectGet(HttpServletRequest request) {
        return ApiResponse.success(httpInspectService.inspect(request, ""));
    }

    @PostMapping
    public ApiResponse<HttpInspectResponse> inspectPost(HttpServletRequest request,
                                                        @RequestBody(required = false) String body) {
        return ApiResponse.success(httpInspectService.inspect(request, body));
    }

    @PutMapping
    public ApiResponse<HttpInspectResponse> inspectPut(HttpServletRequest request,
                                                       @RequestBody(required = false) String body) {
        return ApiResponse.success(httpInspectService.inspect(request, body));
    }

    @DeleteMapping
    public ApiResponse<HttpInspectResponse> inspectDelete(HttpServletRequest request,
                                                          @RequestBody(required = false) String body) {
        return ApiResponse.success(httpInspectService.inspect(request, body));
    }
}
