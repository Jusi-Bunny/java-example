package com.example.deploylab.dto.response;

public record StatusResponse(
        int status,
        String reason,
        String instanceId
) {
}
