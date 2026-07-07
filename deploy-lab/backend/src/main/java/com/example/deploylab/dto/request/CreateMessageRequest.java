package com.example.deploylab.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateMessageRequest(
        @NotBlank(message = "消息标题不能为空")
        @Size(max = 100, message = "消息标题最多 100 个字符")
        String title,

        @Size(max = 2000, message = "消息内容最多 2000 个字符")
        String content,

        Instant expiresAt
) {
}
