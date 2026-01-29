package com.example.langchain4j.llm;

import dev.langchain4j.service.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    /**
     * 测试 LLM 结构化输出
     */
    @Test
    void chatForReport() {
        String userMessage = "你好，我是 Java 开发工程师，请帮我生成对接 LLM 的学习报告";
        AiChatHelperService.Report report = aiChatHelperService.chatForReport(userMessage);
        log.info("report: {}", report);
    }

    @Test
    void chatWithRag() {
        // String result = aiChatHelperService.chat("怎么学习 Java？有哪些常见面试题？");
        // log.info("result: {}", result);

        Result<String> result = aiChatHelperService.chatWithRag("怎么学习 Java？有哪些常见面试题？");
        log.info(result.content());
    }
}
