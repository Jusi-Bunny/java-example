package com.example.deploylab.common;

import java.time.Instant;

public record ApiResponse<T>(
        int code,
        String message,
        T data,
        String traceId,
        Instant timestamp
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", data, TraceContext.traceId(), Instant.now());
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        return new ApiResponse<>(errorCode.getCode(), message, null, TraceContext.traceId(), Instant.now());
    }
}
