package com.example.okhttp.config;

import com.example.okhttp.http.CommonHeaderInterceptor;
import com.example.okhttp.http.OkHttpProperties;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(OkHttpProperties.class)
public class OkHttpConfig {

    @Bean
    public OkHttpClient okHttpClient(OkHttpProperties properties) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        ConnectionPool connectionPool = new ConnectionPool(
                properties.getMaxIdleConnections(),
                properties.getKeepAliveDuration(),
                TimeUnit.SECONDS
        );

        return new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .connectTimeout(Duration.ofSeconds(properties.getConnectTimeout()))
                .readTimeout(Duration.ofSeconds(properties.getReadTimeout()))
                .writeTimeout(Duration.ofSeconds(properties.getWriteTimeout()))
                .callTimeout(Duration.ofSeconds(properties.getCallTimeout()))
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor) // 日志拦截器
                .build();
    }
}