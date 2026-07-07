package com.example.deploylab.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    PARAM_ERROR(40001, HttpStatus.BAD_REQUEST, "请求参数错误"),
    NOT_FOUND(40401, HttpStatus.NOT_FOUND, "数据不存在"),
    DIAGNOSTICS_DISABLED(50301, HttpStatus.SERVICE_UNAVAILABLE, "诊断接口已关闭"),
    INTERNAL_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR, "服务器内部错误");

    private final int code;
    private final HttpStatus status;
    private final String message;

    ErrorCode(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
