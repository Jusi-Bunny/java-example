package com.example.minio.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SYSTEM_ERROR(500, "system error"),
    VALIDATION_ERROR(400, "validation error"),
    BUSINESS_ERROR(1000, "business error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
