package com.example.sse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class SseController {

    @GetMapping("/hello")
    public SseEmitter hello() {
        // 0 表示不超时（不推荐生产直接用 0）
        SseEmitter emitter = new SseEmitter(0L);

        new Thread(() -> {

            try {
                emitter.send("hello world");
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
}
