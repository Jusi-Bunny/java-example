package com.example.satoken.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Jusi
 * @date 2026/10/10
 */
@Data
public class SafeVerifyRequest {

    @NotBlank(message = "密码不能为空")
    private String password;
}
