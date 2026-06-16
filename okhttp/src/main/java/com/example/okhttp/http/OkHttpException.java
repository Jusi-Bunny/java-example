package com.example.okhttp.http;

public class OkHttpException extends RuntimeException {

    public OkHttpException(String message) {
        super(message);
    }

    public OkHttpException(String message, Throwable cause) {
        super(message, cause);
    }
}