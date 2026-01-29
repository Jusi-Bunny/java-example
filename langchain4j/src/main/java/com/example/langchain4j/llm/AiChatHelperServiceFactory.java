package com.example.langchain4j.llm;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiChatHelperServiceFactory {

    @Autowired
    private ChatModel qwenChatModel;

    @Autowired
    private ContentRetriever contentRetriever;

    @Bean
    public AiChatHelperService aiChatHelperService() {

        // 构造基础的 AI Service
        // AiChatHelperService aiChatHelperService = AiServices.create(AiChatHelperService.class, qwenChatModel);

        // 会话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        // 构造带会话记忆的 AI Service
        // AiChatHelperService aiChatHelperService = AiServices.builder(AiChatHelperService.class)
        //         .chatModel(qwenChatModel)
        //         .chatMemory(chatMemory) // 会话记忆
        //         .build();

        // AiChatHelperService aiChatHelperService = AiServices.builder(AiChatHelperService.class)
        //         .chatModel(qwenChatModel)
        //         .chatMemory(chatMemory) // 会话记忆
        //         .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10)) // 通过会话 ID 进行隔离
        //         .build();

        AiChatHelperService aiChatHelperService = AiServices.builder(AiChatHelperService.class)
                .chatModel(qwenChatModel)
                .chatMemory(chatMemory)
                .contentRetriever(contentRetriever) // RAG 检索增强生成
                .build();

        return aiChatHelperService;
    }
}
