package com.example.satoken.common.exception;

import cn.dev33.satoken.exception.*;
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

    @ExceptionHandler(DisableServiceException.class)
    public ResponseEntity<SaResult> handleDisableServiceException(DisableServiceException e) {
        return ResponseEntity.status(403)
                .body(SaResult.code(403).setMsg("账号已被封禁"));
    }

    @ExceptionHandler(NotSafeException.class)
    public ResponseEntity<SaResult> handleNotSafeException(NotSafeException e) {
        // 返回 HTTP 403
        return ResponseEntity.status(403)
                .body(SaResult.code(403).setMsg("请先完成二级认证"));
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<SaResult> handleLoginException(LoginException e) {
        // 返回 HTTP 401
        return ResponseEntity.status(401)
                .body(SaResult.code(401).setMsg(e.getMessage()));
    }

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<SaResult> handleNotLoginException(NotLoginException e) {
        String message = switch (e.getType()) {
            case NotLoginException.NOT_TOKEN -> "请先登录";
            case NotLoginException.INVALID_TOKEN -> "登录状态异常，请重新登录";
            case NotLoginException.TOKEN_TIMEOUT -> "登录状态已过期，请重新登录";
            case NotLoginException.BE_REPLACED -> "账号已在其他设备登录";
            case NotLoginException.KICK_OUT -> "您已被管理员强制下线";
            case NotLoginException.TOKEN_FREEZE -> "登录状态因长时间未操作已失效，请重新登录";
            case NotLoginException.NO_PREFIX -> NotLoginException.NO_PREFIX_MESSAGE;
            case null, default -> "登录状态已失效，请重新登录";
        };
        // 返回 HTTP 401
        return ResponseEntity.status(401)
                .body(SaResult.code(401).setMsg(message));
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

    @ExceptionHandler(NotRoleException.class)
    public ResponseEntity<SaResult> handleNotRoleException(NotRoleException e) {
        // 返回 HTTP 403
        return ResponseEntity.status(403)
                .body(SaResult.code(403).setMsg("无权访问"));
    }

    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<SaResult> handleNotPermissionException(NotPermissionException e) {
        // 返回 HTTP 403
        return ResponseEntity.status(403)
                .body(SaResult.code(403).setMsg("无权访问"));
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