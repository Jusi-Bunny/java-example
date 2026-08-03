package com.example.i18n.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class I18nMessageService {

    private final MessageSource messageSource;

    /**
     * 使用当前请求的 Locale 获取消息。
     */
    public String get(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * 使用指定 Locale 获取消息。
     */
    public String get(Locale locale, String code, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * 解析 FieldError、ObjectError 等 Spring 错误对象。
     */
    public String get(MessageSourceResolvable resolvable) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(resolvable, locale);
    }
}