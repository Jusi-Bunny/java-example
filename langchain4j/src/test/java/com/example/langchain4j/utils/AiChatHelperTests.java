package com.example.langchain4j.utils;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AiChatHelperTests {

    @Autowired
    private AiChatHelper aiChatHelper;

    @Test
    void chat() {
        String chat = aiChatHelper.chat("你好，我是 Java 开发工程师");
    }

    @Test
    void chatWithMessage() {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("描述图片内容"),
                ImageContent.from("https://www.codefather.cn/logo.png")
        );
        String chat = aiChatHelper.chat(userMessage);
    }

    @Test
    void chatWithSystemMessage() {
        String chat = aiChatHelper.chatWithSystemMessage("你好，我是 Java 开发工程师");
    }
}
