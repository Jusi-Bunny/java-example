package com.example.deploylab.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record HttpInspectResponse(
        String method,
        String uri,
        Map<String, List<String>> queryParameters,
        Map<String, List<String>> headers,
        String contentType,
        String body,
        boolean bodyTruncated,
        String clientIp,
        String xRealIp,
        String xForwardedFor,
        String xForwardedProto,
        String host,
        String userAgent,
        String instanceId,
        Instant receivedAt
) {
}
