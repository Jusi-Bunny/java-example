package com.example.langchain4j.utils;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiChatHelperServiceFactory {

    @Autowired
    private ChatModel qwenChatModel;

    @Bean
    public AiChatHelperService aiChatHelperService() {
        // 会话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        // 构造 AI Service
        // AiServices.create(AiChatHelperService.class, qwenChatModel);

        // 构造带会话记忆的 AI Service
        // AiChatHelperService aiChatHelperService = AiServices.builder(AiChatHelperService.class)
        //         .chatModel(qwenChatModel)
        //         .chatMemory(chatMemory) // 会话记忆
        //         .build();

        AiChatHelperService aiChatHelperService = AiServices.builder(AiChatHelperService.class)
                .chatModel(qwenChatModel)
                .chatMemory(chatMemory) // 会话记忆
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10)) // 通过会话 ID 进行隔离
                .build();

        return aiChatHelperService;
    }
}
