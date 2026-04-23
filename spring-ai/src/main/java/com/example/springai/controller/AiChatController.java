package com.example.springai.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.springai.app.AiChatApp;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/ai")
public class AiChatController {

    @Autowired
    private AiChatApp aiChatApp;

    /**
     * SSE 流式调用 AI 恋爱大师应用
     *
     * @param message        用户消息
     * @param conversationId 会话 ID
     * @return SseEmitter
     */
    @Operation(summary = "2 - 通过 SSE 获取对话结果")
    @GetMapping(value = "/chat/stream")
    public SseEmitter doChatWithLoveAppServerSseEmitter(String message, String conversationId) {
        // 创建一个超时时间较长的 SseEmitter
        SseEmitter sseEmitter = new SseEmitter(180000L); // 3 分钟超时

        // 获取 Flux 响应式数据流并且直接通过订阅推送给 SseEmitter
        this.aiChatApp.doChatByStream(message, conversationId)
                .subscribe(chunk -> {
                    try {
                        log.info("SSE 发送数据：{}", chunk);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("conversationId", conversationId);
                        jsonObject.put("type", "ANSWER");
                        jsonObject.put("content", chunk);
                        jsonObject.put("timestamp", System.currentTimeMillis());
                        sseEmitter.send(
                                SseEmitter.event()
                                        .id(conversationId)
                                        .name("message")
                                        .data(jsonObject)
                        );
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        // 返回
        return sseEmitter;
    }
}
