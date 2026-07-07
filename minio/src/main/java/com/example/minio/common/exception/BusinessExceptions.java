package com.example.minio.common.exception;


import com.example.minio.common.error.ErrorCode;

public final class BusinessExceptions {

    private BusinessExceptions() {
    }

    public static BusinessException notFound(String resourceName) {
        return new BusinessException(ErrorCode.BUSINESS_ERROR, resourceName + " does not exist");
    }

    public static BusinessException invalid(String message) {
        return new BusinessException(ErrorCode.BUSINESS_ERROR, message);
    }
}
