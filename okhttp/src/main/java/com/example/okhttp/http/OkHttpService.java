package com.example.okhttp.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OkHttpService {

    private final OkHttpClient okHttpClient;

    private final ObjectMapper objectMapper;

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public String get(String url) {
        return get(url, Map.of());
    }

    public String get(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get();

        addHeaders(builder, headers);

        return execute(builder.build());
    }

    public String postJson(String url, Object body) {
        return postJson(url, body, Map.of());
    }

    public String postJson(String url, Object body, Map<String, String> headers) {
        String json = toJson(body);

        RequestBody requestBody = RequestBody.create(json, JSON);

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);

        addHeaders(builder, headers);

        return execute(builder.build());
    }

    public <T> T postJson(String url, Object body, Class<T> responseType) {
        String response = postJson(url, body);
        return fromJson(response, responseType);
    }

    public <T> T get(String url, Class<T> responseType) {
        String response = get(url);
        return fromJson(response, responseType);
    }

    private String execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String bodyString = responseBody == null ? "" : responseBody.string();

            if (!response.isSuccessful()) {
                throw new OkHttpException(
                        "HTTP 请求失败，状态码：" + response.code() + "，响应内容：" + bodyString
                );
            }

            return bodyString;
        } catch (IOException e) {
            throw new OkHttpException("HTTP 请求异常：" + e.getMessage(), e);
        }
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new OkHttpException("对象序列化 JSON 失败", e);
        }
    }

    private <T> T fromJson(String json, Class<T> responseType) {
        try {
            return objectMapper.readValue(json, responseType);
        } catch (JsonProcessingException e) {
            throw new OkHttpException("JSON 反序列化失败，响应内容：" + json, e);
        }
    }

    private void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return;
        }

        headers.forEach(builder::addHeader);
    }

    public String postForm(String url, Map<String, String> formParams) {
        return postForm(url, formParams, Map.of());
    }

    public String postForm(String url,
                           Map<String, String> formParams,
                           Map<String, String> headers) {
        FormBody.Builder formBuilder = new FormBody.Builder();

        if (formParams != null) {
            formParams.forEach(formBuilder::add);
        }

        RequestBody requestBody = formBuilder.build();

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);

        addHeaders(builder, headers);

        return execute(builder.build());
    }

    public String uploadFile(String url,
                             String fileParamName,
                             File file,
                             Map<String, String> formParams,
                             Map<String, String> headers) {
        RequestBody fileBody = RequestBody.create(
                file,
                MediaType.parse("application/octet-stream")
        );

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileParamName, file.getName(), fileBody);

        if (formParams != null) {
            formParams.forEach(multipartBuilder::addFormDataPart);
        }

        RequestBody requestBody = multipartBuilder.build();

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);

        addHeaders(builder, headers);

        return execute(builder.build());
    }
}