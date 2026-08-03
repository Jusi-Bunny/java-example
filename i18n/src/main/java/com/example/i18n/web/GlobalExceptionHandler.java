package com.example.i18n.web;

import com.example.i18n.error.BusinessException;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.example.i18n.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException exception) {

        ErrorCode errorCode = exception.getErrorCode();

        String localizedMessage = message(
                errorCode.getMessageKey(),
                exception.getMessageArgs()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiError.business(errorCode.name(), localizedMessage));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception) {

        Locale locale = LocaleContextHolder.getLocale();

        Map<String, String> fieldErrors = new LinkedHashMap<>();

        for (FieldError fieldError
                : exception.getBindingResult().getFieldErrors()) {

            String localizedMessage =
                    messageSource.getMessage(fieldError, locale);

            fieldErrors.putIfAbsent(fieldError.getField(), localizedMessage);
        }

        return ResponseEntity
                .badRequest()
                .body(new ApiError("VALIDATION_FAILED", message("validation.failed"), fieldErrors));
    }

    private String message(String key, Object... arguments) {

        Locale locale = LocaleContextHolder.getLocale();

        try {
            return messageSource.getMessage(key, arguments, locale);
        } catch (NoSuchMessageException exception) {
            log.error("Missing i18n message: key={}, locale={}", key, locale, exception);
            return key;
        }
    }
}
