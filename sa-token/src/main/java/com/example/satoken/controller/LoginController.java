package com.example.satoken.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.satoken.request.LoginRequest;
import com.example.satoken.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jusi
 * @date 2026/8/10
 */
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping("/login")
    public SaResult login(@Valid @RequestBody LoginRequest request) {
        Long userId = authService.authenticate(request.getUsername(), request.getPassword());
        StpUtil.login(userId);
        return SaResult.data(StpUtil.getTokenInfo());
    }

    @GetMapping("/me")
    public SaResult currentUser() {
        StpUtil.checkLogin();
        // 能执行到这里，表示当前请求存在有效登录态
        Object loginId = StpUtil.getLoginId();
        return SaResult.data(loginId);
    }

    @PostMapping("/logout")
    public SaResult logout() {
        StpUtil.checkLogin();
        StpUtil.logout();
        return SaResult.ok("退出成功");
    }

    @PostMapping("/roles")
    public SaResult getRoles() {
        StpUtil.checkLogin();
        return SaResult.data(StpUtil.getRoles());
    }

    @PostMapping("/permissions")
    public SaResult getPermissions() {
        StpUtil.checkLogin();
        return SaResult.data(StpUtil.getPermissions());
    }
}
