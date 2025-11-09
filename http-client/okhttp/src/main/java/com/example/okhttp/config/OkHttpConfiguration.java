package com.example.okhttp.config;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfiguration {

    @Value("${ok.http.connect-timeout}")
    private Integer connectTimeout;
    @Value("${ok.http.read-timeout}")
    private Integer readTimeout;
    @Value("${ok.http.write-timeout}")
    private Integer writeTimeout;
    @Value("${ok.http.max-idle-connections}")
    private Integer maxIdleConnections;
    @Value("${ok.http.keep-alive-duration}")
    private Long keepAliveDuration;

    @Bean
    public OkHttpClient okHttpClient() {
        // 日志拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 连接超时
                .readTimeout(30, TimeUnit.SECONDS) // 读取超时
                .writeTimeout(30, TimeUnit.SECONDS) // 写入超时
                .retryOnConnectionFailure(true) // 失败自动重连
                .addInterceptor(logging) // 添加日志拦截器
                .build();
    }
}
