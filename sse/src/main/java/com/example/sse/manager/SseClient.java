package com.example.sse.manager;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Data
@Builder
public class SseClient {

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 连接 ID（建议 UUID）
     */
    private String connectionId;

    /**
     * SSE 发射器
     */
    private SseEmitter emitter;

    /**
     * 建立连接时间
     */
    private long connectTime;

    /**
     * 最后一次发送时间
     */
    private volatile long lastSendTime;
}
