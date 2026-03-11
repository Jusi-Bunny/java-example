package com.example.sse.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseManager {

    /**
     * userId -> (connectionId -> client)
     */
    private final Map<String, Map<String, SseClient>> userConnections = new ConcurrentHashMap<>();

    /**
     * 连接超时时间
     * 0L 表示由外部容器/代理控制
     */
    private static final long SSE_TIMEOUT = 0L;

    /**
     * 创建连接
     */
    public SseEmitter connect(String userId) {
        String connectionId = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);

        SseClient client = SseClient.builder()
                .userId(userId)
                .connectionId(connectionId)
                .emitter(emitter)
                .connectTime(System.currentTimeMillis())
                .lastSendTime(System.currentTimeMillis())
                .build();

        userConnections
                .computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                .put(connectionId, client);

        registerLifecycle(client);

        // 建连成功先推一条 connect 事件
        sendToConnection(userId, connectionId, "connect",
                SseMessage.builder()
                        .type("connect")
                        .event("connect")
                        .data(connectionId)
                        .finished(false)
                        .timestamp(System.currentTimeMillis())
                        .build());

        log.info("SSE connected: userId={}, connectionId={}, onlineConnections={}",
                userId, connectionId, countConnections(userId));

        return emitter;
    }

    /**
     * 注册生命周期回调
     */
    private void registerLifecycle(SseClient client) {
        String userId = client.getUserId();
        String connectionId = client.getConnectionId();
        SseEmitter emitter = client.getEmitter();

        emitter.onCompletion(() -> {
            removeConnection(userId, connectionId);
            log.info("SSE onCompletion: userId={}, connectionId={}", userId, connectionId);
        });

        emitter.onTimeout(() -> {
            removeConnection(userId, connectionId);
            log.warn("SSE onTimeout: userId={}, connectionId={}", userId, connectionId);
            try {
                emitter.complete();
            } catch (Exception ignored) {
            }
        });

        emitter.onError(ex -> {
            removeConnection(userId, connectionId);
            log.warn("SSE onError: userId={}, connectionId={}, message={}",
                    userId, connectionId, ex == null ? null : ex.getMessage());
        });
    }

    /**
     * 给单个连接发送消息
     */
    public boolean sendToConnection(String userId, String connectionId, String eventName, Object data) {
        SseClient client = getClient(userId, connectionId);
        if (client == null) {
            return false;
        }

        try {
            client.getEmitter().send(SseEmitter.event()
                    .name(eventName)
                    .id(connectionId + "-" + System.currentTimeMillis())
                    .data(data));
            client.setLastSendTime(System.currentTimeMillis());
            return true;
        } catch (IOException e) {
            // send 失败通常说明客户端已断开，清理连接即可
            log.warn("SSE send failed: userId={}, connectionId={}, eventName={}, message={}",
                    userId, connectionId, eventName, e.getMessage());
            removeConnection(userId, connectionId);
            return false;
        } catch (Exception e) {
            log.error("SSE send error: userId={}, connectionId={}, eventName={}",
                    userId, connectionId, eventName, e);
            removeConnection(userId, connectionId);
            return false;
        }
    }

    /**
     * 给某个用户的所有连接发送消息
     */
    public int sendToUser(String userId, String eventName, Object data) {
        Map<String, SseClient> connections = userConnections.get(userId);
        if (connections == null || connections.isEmpty()) {
            return 0;
        }

        int success = 0;
        List<String> connectionIds = new ArrayList<>(connections.keySet());
        for (String connectionId : connectionIds) {
            boolean ok = sendToConnection(userId, connectionId, eventName, data);
            if (ok) {
                success++;
            }
        }
        return success;
    }

    /**
     * 广播消息
     */
    public int broadcast(String eventName, Object data) {
        int total = 0;
        List<String> userIds = new ArrayList<>(userConnections.keySet());
        for (String userId : userIds) {
            total += sendToUser(userId, eventName, data);
        }
        return total;
    }

    /**
     * 主动关闭某个连接
     */
    public void closeConnection(String userId, String connectionId) {
        SseClient client = getClient(userId, connectionId);
        if (client == null) {
            return;
        }

        removeConnection(userId, connectionId);
        try {
            client.getEmitter().complete();
        } catch (Exception ignored) {
        }

        log.info("SSE closed manually: userId={}, connectionId={}", userId, connectionId);
    }

    /**
     * 主动关闭某个用户的全部连接
     */
    public void closeUserConnections(String userId) {
        Map<String, SseClient> connections = userConnections.remove(userId);
        if (connections == null || connections.isEmpty()) {
            return;
        }

        for (SseClient client : connections.values()) {
            try {
                client.getEmitter().complete();
            } catch (Exception ignored) {
            }
        }

        log.info("SSE all user connections closed: userId={}, count={}", userId, connections.size());
    }

    /**
     * 删除连接
     */
    private void removeConnection(String userId, String connectionId) {
        Map<String, SseClient> connections = userConnections.get(userId);
        if (connections == null) {
            return;
        }

        connections.remove(connectionId);

        if (connections.isEmpty()) {
            userConnections.remove(userId);
        }
    }

    /**
     * 获取连接
     */
    public SseClient getClient(String userId, String connectionId) {
        Map<String, SseClient> connections = userConnections.get(userId);
        if (connections == null) {
            return null;
        }
        return connections.get(connectionId);
    }

    /**
     * 查询用户连接数
     */
    public int countConnections(String userId) {
        Map<String, SseClient> connections = userConnections.get(userId);
        return connections == null ? 0 : connections.size();
    }

    /**
     * 当前总连接数
     */
    public int totalConnections() {
        return userConnections.values().stream()
                .mapToInt(Map::size)
                .sum();
    }

    /**
     * 当前在线用户数
     */
    public int totalUsers() {
        return userConnections.size();
    }
}
