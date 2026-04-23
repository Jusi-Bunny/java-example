package com.example.sse.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SseMessage<T> {

    /**
     * connect / token / progress / finish / error / heartbeat
     */
    private String type;

    /**
     * 事件名
     */
    private String event;

    /**
     * 数据体
     */
    private T data;

    /**
     * 是否结束
     */
    private Boolean finished;

    /**
     * 时间戳
     */
    private Long timestamp;
}