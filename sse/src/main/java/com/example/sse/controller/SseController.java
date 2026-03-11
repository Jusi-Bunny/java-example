package com.example.sse.controller;

import com.example.sse.manager.SseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseManager sseManager;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@RequestHeader("userId") String userId) {
        //
        return sseManager.connect(userId);
    }

    // =================================================================================================================

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @GetMapping(path = "/hello", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter hello() {
        // 0 表示不超时（不推荐生产直接用 0）
        // SseEmitter emitter = new SseEmitter(0L);
        SseEmitter emitter = new SseEmitter();

        executor.scheduleAtFixedRate(() -> {
            try {
                // 发送事件
                emitter.send("Hello at " + System.currentTimeMillis());
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

        // 监听完成事件
        emitter.onCompletion(executor::shutdown);

        return emitter;
    }

    @GetMapping("/json")
    public SseEmitter json() {
        SseEmitter emitter = new SseEmitter(0L);
        new Thread(() -> {
            try {
                emitter.send(
                        "{\"msg\":\"hello\"}"
                );

                Thread.sleep(1000);

                emitter.send(
                        "{\"msg\":\"hello\"}",
                        MediaType.APPLICATION_JSON
                );
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }

    @GetMapping("/connect")
    public SseEmitter connect() {
        SseEmitter emitter = new SseEmitter(0L);

        new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    emitter.send(
                            SseEmitter.event()
                                    .id(String.valueOf(i))
                                    .name("message")
                                    .data("第 " + i + " 条消息")
                    );
                    Thread.sleep(1000);
                }
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }

    @GetMapping("/event")
    public SseEmitter event() {
        SseEmitter emitter = new SseEmitter(0L);

        new Thread(() -> {

            try {
                emitter.send(
                        SseEmitter.event()
                                .name("progress")
                                .data(30)
                );

                Thread.sleep(1000);

                emitter.send(
                        SseEmitter.event()
                                .name("finish")
                                .data("done")
                );
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }

    @GetMapping("/lifecycle")
    public SseEmitter lifecycle() {

        SseEmitter emitter = new SseEmitter(0L);

        emitter.onCompletion(() -> log.info("onCompletion"));
        emitter.onTimeout(() -> log.info("onTimeout"));
        emitter.onError((e) -> log.error("onError: {}", e.getMessage()));

        new Thread(() -> {
            try {

                for (int i = 1; i <= 5; i++) {
                    emitter.send(
                            SseEmitter.event()
                                    .id(String.valueOf(i))
                                    .name("message")
                                    .data("第 " + i + " 条消息")
                    );

                    Thread.sleep(1000);

                    if (i == 2) {
                        int j = 1 / 0; // 模拟异常
                    }
                }
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}
