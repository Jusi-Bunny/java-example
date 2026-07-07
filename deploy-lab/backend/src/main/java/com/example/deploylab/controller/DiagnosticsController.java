package com.example.deploylab.controller;

import com.example.deploylab.common.ApiResponse;
import com.example.deploylab.common.BusinessException;
import com.example.deploylab.common.ErrorCode;
import com.example.deploylab.config.DeployLabProperties;
import com.example.deploylab.dto.response.DelayResponse;
import com.example.deploylab.dto.response.StatusResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Validated
@RestController
@RequestMapping("/api/diagnostics")
public class DiagnosticsController {

    private static final Set<Integer> ALLOWED_STATUS_CODES = Set.of(200, 400, 401, 403, 404, 429, 500, 502, 503, 504);

    private final DeployLabProperties properties;

    public DiagnosticsController(DeployLabProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/delay")
    public ApiResponse<DelayResponse> delay(@RequestParam(defaultValue = "1000") @Min(0) @Max(5000) long milliseconds)
            throws InterruptedException {
        ensureDiagnosticsEnabled();
        long start = System.currentTimeMillis();
        Thread.sleep(milliseconds);
        long actual = System.currentTimeMillis() - start;
        return ApiResponse.success(new DelayResponse(milliseconds, actual, properties.getInstanceId()));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<StatusResponse>> status(@RequestParam(defaultValue = "503") int code) {
        ensureDiagnosticsEnabled();
        if (!ALLOWED_STATUS_CODES.contains(code)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "不支持的诊断状态码");
        }
        HttpStatus status = HttpStatus.valueOf(code);
        StatusResponse response = new StatusResponse(code, status.getReasonPhrase(), properties.getInstanceId());
        return ResponseEntity.status(status).body(ApiResponse.success(response));
    }

    @GetMapping("/error")
    public ApiResponse<Void> error() {
        ensureDiagnosticsEnabled();
        throw new IllegalStateException("Diagnostics error triggered");
    }

    private void ensureDiagnosticsEnabled() {
        if (!properties.getDiagnostics().isEnabled()) {
            throw new BusinessException(ErrorCode.DIAGNOSTICS_DISABLED);
        }
    }
}
