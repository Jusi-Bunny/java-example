package com.example.langchain4j.llm;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

import java.util.List;

// @AiService
public interface AiChatHelperService {

    /**
     * 简单对话，附带 SystemMessage
     *
     * @param userMessage 用户消息
     * @return AI 输出
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String userMessage);

    /**
     * 带会话记忆的聊天（通过 MemoryId 进行隔离）
     *
     * @param memoryId    会话记忆 ID
     * @param userMessage 用户消息
     * @return AI 输出
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    String chatWithMemory(@MemoryId String memoryId, String userMessage);

    /**
     * 结构化输出
     *
     * @param userMessage 用户消息
     * @return AI 输出
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatForReport(String userMessage);

    // 学习报告
    record Report(String name, List<String> suggerstionList) {
    }

    /**
     * 带 RAG 的聊天
     *
     * @param userMessage 用户消息
     * @return AI 输出
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    Result<String> chatWithRag(String userMessage);

    /**
     * 流式输出
     *
     * @param memoryId    会话记忆 ID
     * @param userMessage 用户消息
     * @return AI 输出
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    Flux<String> chatStream(@MemoryId int memoryId, @UserMessage String userMessage);
}
