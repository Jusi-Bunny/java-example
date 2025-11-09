package com.example.okhttp;

import com.alibaba.fastjson2.JSON;
import com.example.okhttp.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = "spring.output.ansi.enabled=ALWAYS")
public class OkHttpTest {

    @Autowired
    private OkHttpClient client;

    @Autowired
    private OkHttpUtils okHttpUtils;

    @Test
    public void testGet() throws Exception {
        String url = "http://httpbin.org/get";
        String result = okHttpUtils.get(url, null);
        log.info("result: {}", result);
    }

    @Test
    public void testPostJson() throws Exception {
        String url = "http://httpbin.org/post";

        // 构建 JSON 参数
        Map<String, Object> params = new HashMap<>();
        params.put("title", "foo");
        params.put("body", "bar");
        params.put("userId", 1);

        String jsonBody = JSON.toJSONString(params);

        // 构建请求体
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody, mediaType);

        // 构建请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Content-Type", "application/json")
                .build();

        // 发送请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            log.info("响应对象：{}", response);
            log.info("响应对象体：{}", response.body());

            // 解析响应 JSON
            String resp = response.body().string();
            Map<String, Object> result = JSON.parseObject(resp, Map.class);

            log.info("响应结果：{}", result);
        }
    }
}
