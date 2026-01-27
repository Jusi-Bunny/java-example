package com.example.langchain4j.utils;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

import java.util.List;

// @AiService
public interface AiChatHelperService {

    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String userMessage);

    /**
     * 带会话记忆的聊天
     *
     * @param memoryId    会话记忆 ID
     * @param userMessage 用户消息
     * @return AI 输出
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    String chatWithMemory(@MemoryId String memoryId, String userMessage);

    @SystemMessage(fromResource = "system-prompt.txt")
    Report report(String userMessage);

    // 学习报告
    record Report(String name, List<String> suggerstionList) {
    }
}
