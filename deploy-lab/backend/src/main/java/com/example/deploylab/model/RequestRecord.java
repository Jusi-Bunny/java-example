package com.example.deploylab.model;

import java.time.Instant;

public record RequestRecord(
        Long id,
        String traceId,
        String method,
        String path,
        Integer status,
        Long durationMs,
        String clientIp,
        String instanceId,
        Instant createdAt
) {
}
