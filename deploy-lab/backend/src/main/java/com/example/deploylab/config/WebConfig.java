package com.example.deploylab.config;

import com.example.deploylab.interceptor.RequestRecordInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestRecordInterceptor requestRecordInterceptor;

    public WebConfig(RequestRecordInterceptor requestRecordInterceptor) {
        this.requestRecordInterceptor = requestRecordInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestRecordInterceptor);
    }
}
