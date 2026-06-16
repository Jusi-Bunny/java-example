package com.example.okhttp.http;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "okhttp")
public class OkHttpProperties {

    /**
     * 连接超时时间，单位：秒
     */
    private long connectTimeout = 10;

    /**
     * 读取超时时间，单位：秒
     */
    private long readTimeout = 30;

    /**
     * 写入超时时间，单位：秒
     */
    private long writeTimeout = 30;

    /**
     * 整个请求调用超时时间，单位：秒
     */
    private long callTimeout = 60;

    /**
     * 最大空闲连接数
     */
    private int maxIdleConnections = 20;

    /**
     * 连接保活时间，单位：秒
     */
    private long keepAliveDuration = 300;
}