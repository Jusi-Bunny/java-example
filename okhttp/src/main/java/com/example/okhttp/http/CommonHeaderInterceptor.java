package com.example.okhttp.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 添加通用请求头
 */
public class CommonHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request oldRequest = chain.request();

        Request newRequest = oldRequest.newBuilder()
                .addHeader("User-Agent", "demo-spring-boot")
                .addHeader("X-Client", "okhttp")
                .build();

        return chain.proceed(newRequest);
    }
}