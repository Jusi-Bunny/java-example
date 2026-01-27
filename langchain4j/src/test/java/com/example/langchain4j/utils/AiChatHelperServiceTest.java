package com.example.langchain4j.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AiChatHelperServiceTest {

    @Autowired
    private AiChatHelperService aiChatHelperService;

    @Test
    void chat() {
        String chat = aiChatHelperService.chat("你好，我是 Java 开发工程师");
        log.info("chat: {}", chat);
    }

    /**
     * 测试带会话记忆的聊天
     */
    @Test
    void chatWithMemory() {
        String result = aiChatHelperService.chat("你好，我是 Java 开发工程师");
        log.info("result: {}", result);
        result = aiChatHelperService.chat("你好，我是使用什么语言进行开发的？请推荐几个这个语言常用的开发框架。");
        log.info("result: {}", result);
    }
}