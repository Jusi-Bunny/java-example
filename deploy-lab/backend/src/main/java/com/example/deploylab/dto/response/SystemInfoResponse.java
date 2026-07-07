package com.example.deploylab.dto.response;

import java.time.Instant;

public record SystemInfoResponse(
        String applicationName,
        String appVersion,
        String instanceId,
        String activeProfile,
        Instant startedAt,
        long uptimeSeconds,
        String javaVersion,
        String osName,
        String hostname,
        String containerName,
        int serverPort,
        Instant currentTime,
        int availableProcessors,
        long usedMemoryBytes,
        long maxMemoryBytes,
        boolean diagnosticsEnabled
) {
}
