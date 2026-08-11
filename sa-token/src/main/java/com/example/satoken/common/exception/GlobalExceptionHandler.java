package com.example.satoken.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<SaResult> handleLoginException(LoginException e) {
        // 返回 HTTP 401
        return ResponseEntity.status(401)
                .body(SaResult.code(401).setMsg(e.getMessage()));
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<SaResult> handleNotLoginException(NotLoginException e) {
        // 返回 HTTP 401
        return ResponseEntity.status(401)
                .body(SaResult.code(401).setMsg("登录状态已失效，请重新登录"));
    }

    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<SaResult> handleAccountDisabledException(AccountDisabledException e) {
        // 返回 HTTP 403
        return ResponseEntity.status(403)
                .body(SaResult.code(403).setMsg(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SaResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 返回 HTTP 400
        return ResponseEntity.status(400)
                .body(SaResult.code(400).setMsg(extractFieldErrorMessage(e.getFieldError())));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<SaResult> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        // 返回 HTTP 400
        return ResponseEntity.status(400)
                .body(SaResult.code(400).setMsg("请求参数缺失或格式错误"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SaResult> handleUnknownException(Exception e) {
        // 服务端记录完整异常
        // 前端只返回 HTTP 500 和「系统繁忙」
        log.error("未处理的系统异常", e);
        return ResponseEntity.status(500).body(SaResult.error("系统繁忙，请稍后再试"));
    }

    private String extractFieldErrorMessage(FieldError fieldError) {
        return Optional.ofNullable(fieldError)
                .map(FieldError::getDefaultMessage)
                .filter(message -> !message.isBlank())
                .orElse("参数校验异常");
    }
}