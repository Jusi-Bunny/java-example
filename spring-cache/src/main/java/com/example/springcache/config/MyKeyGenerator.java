package com.example.springcache.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Jusi
 * @date 2026/9/24
 */
@Component
public class MyKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return null;
    }
}
