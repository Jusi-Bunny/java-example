package com.example.langchain4j;

import com.example.langchain4j.utils.AiChatHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LangChain4jApplicationTests {

    @Autowired
    private AiChatHelper aiChatHelper;

    @Test
    void chat() {
        aiChatHelper.chat("你好，我是 Java 开发工程师");
    }
}
