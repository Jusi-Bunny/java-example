package com.example.okhttp;

import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class OkhttpApplicationTests {

    private static final OkHttpClient CLIENT = new OkHttpClient();

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    /**
     * GET 请求
     */
    @Test
    public void okHttpGetDemo() throws IOException {

        Request request = new Request.Builder()
                .url("http://httpbin.org/get")
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("请求失败，状态码：" + response.code());
            }

            String result = response.body().string();
            System.out.println(result);
        }
    }

    /**
     * 带 Header 的请求
     */
    @Test
    public void okHttpHeaderDemo() throws IOException {
        Request request = new Request.Builder()
                .url("http://httpbin.org/get")
                .addHeader("Authorization", "Bearer xxx")
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
    }

    /**
     * POST 请求
     */
    @Test
    public void okHttpPostDemo() throws IOException {
        String json = """
                {
                  "username": "tom",
                  "age": 18
                }
                """;

        RequestBody requestBody = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("http://httpbin.org/post")
                .post(requestBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("请求失败，状态码：" + response.code());
            }

            String result = response.body().string();
            System.out.println(result);
        }
    }
}
