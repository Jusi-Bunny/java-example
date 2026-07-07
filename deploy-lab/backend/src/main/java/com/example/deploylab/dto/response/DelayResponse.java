package com.example.deploylab.dto.response;

public record DelayResponse(
        long requestedMilliseconds,
        long actualMilliseconds,
        String instanceId
) {
}
