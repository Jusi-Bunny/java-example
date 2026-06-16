package com.example.okhttp.http;

import com.example.okhttp.client.user.CreateUserRequest;
import com.example.okhttp.client.user.CreateUserResponse;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Map;

@SpringBootTest
class OkHttpServiceTest {

    @Autowired
    private OkHttpService okHttpService;

    @Test
    public void testGet() {
        String result = okHttpService.get("https://httpbin.org/get");
        System.out.println(result);
    }

    @Test
    public void testPost() {
        String json = """
                {
                  "name": "Tom",
                  "age": 18
                }
                """;

        String result = okHttpService.postJson(
                "https://httpbin.org/post",
                json,
                Map.of("Authorization", "Bearer xxx")
        );
        System.out.println(result);
    }

    @Test
    public void testPostJson() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Tom");
        request.setAge(18);

        CreateUserResponse response = okHttpService.postJson(
                "https://httpbin.org/post",
                request,
                CreateUserResponse.class
        );
        System.out.println(response);
    }

    @Test
    public void testPostForm() {
        Map<String, String> formParams = Map.of(
                "name", "Tom",
                "age", "18"
        );

        String result = okHttpService.postForm(
                "https://httpbin.org/post",
                formParams,
                Map.of("Authorization", "Bearer xxx")
        );
        System.out.println(result);
    }

    @Test
    public void testUploadFile() {
        File file = new File("/tmp/test.mp3");

        String result = okHttpService.uploadFile(
                "https://api.example.com/upload",
                "file",
                file,
                Map.of("bizType", "audio"),
                Map.of("Authorization", "Bearer xxx")
        );
    }
}
