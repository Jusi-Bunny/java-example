package com.example.deploylab.model;

import java.time.Instant;

public record Message(
        Long id,
        String title,
        String content,
        Instant createdAt,
        Instant expiresAt
) {
}
