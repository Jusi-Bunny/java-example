package com.example.sse.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseHeartbeatService {

    private final SseManager sseManager;

    @Scheduled(fixedDelay = 20000)
    public void heartbeat() {
        int count = sseManager.broadcast("heartbeat",
                SseMessage.builder()
                        .type("heartbeat")
                        .event("heartbeat")
                        .data("ping")
                        .finished(false)
                        .timestamp(System.currentTimeMillis())
                        .build());

        if (count > 0) {
            log.debug("SSE heartbeat sent, count={}", count);
        }
    }
}