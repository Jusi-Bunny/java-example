package com.example.minio.common.result;

import com.example.minio.common.error.ErrorCode;
import com.example.minio.common.exception.BusinessException;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final int SUCCESS_CODE = 0;
    public static final String SUCCESS_MESSAGE = "success";

    private final int code;
    private final String message;
    private final T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result<Void> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static <T> Result<T> error(int code, String message) {
        if (isOk(code)) {
            throw new IllegalArgumentException("error code must not be success code");
        }
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> Result<T> error(Result<?> result) {
        Objects.requireNonNull(result, "result must not be null");
        return error(result.getCode(), result.getMessage());
    }

    public static boolean isOk(int code) {
        return code == SUCCESS_CODE;
    }

    public static boolean isError(int code) {
        return !isOk(code);
    }

    public boolean isOk() {
        return isOk(code);
    }

    public boolean isError() {
        return isError(code);
    }

    public void checkFailure() {
        if (isError()) {
            throw new BusinessException(code, message);
        }
    }

    public T checkedData() {
        checkFailure();
        return data;
    }
}