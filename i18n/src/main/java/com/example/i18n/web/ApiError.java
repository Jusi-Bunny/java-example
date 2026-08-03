package com.example.i18n.web;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ApiError {

    private String code;
    private String message;
    private Map<String, String> fieldErrors;

    public ApiError(String code, String message, Map<String, String> fieldErrors) {
        this.code = code;
        this.message = message;
        this.fieldErrors = fieldErrors == null ? Map.of() : Map.copyOf(fieldErrors);
    }

    public static ApiError business(String code, String message) {
        return new ApiError(code, message, Map.of());
    }
}
