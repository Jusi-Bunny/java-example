package com.example.base.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**") // 允许所有接口
    //             .allowedOrigins("http://localhost:5173") // 允许所有前端源
    //             .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    //             .allowedHeaders("*") // 允许所有请求头
    //             .allowCredentials(true) // 允许携带身份凭证
    //             .maxAge(3600); // 预检请求缓存 1 小时
    // }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有接口
                .allowedMethods("*") // 允许所有请求方法
                .allowedOriginPatterns("*") // 允许所有前端源
                .allowCredentials(true) // 允许携带身份凭证
                .allowedHeaders("*") // 允许所有请求头
                .maxAge(3600); // 预检请求缓存 1 小时
    }
}