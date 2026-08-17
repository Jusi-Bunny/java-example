package com.example.satoken.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author Jusi
 * @date 2026/8/10
 */
@Data
public class LoginRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "设备类型不能为空")
    @Pattern(regexp = "^(PC|APP)$", message = "设备类型格式错误")
    private String deviceType;
}
