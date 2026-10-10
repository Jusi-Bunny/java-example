package com.example.satoken.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
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
        StpUtil.checkDisable(userId);
        StpUtil.login(userId, SaLoginParameter.create().setDeviceType(request.getDeviceType()));
        return SaResult.data(StpUtil.getTokenInfo());
    }

    @GetMapping("/me")
    public SaResult currentUser() {
        // 能执行到这里，表示当前请求存在有效登录态
        Object loginId = StpUtil.getLoginId();
        return SaResult.data(loginId);
    }

    @PostMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("退出成功");
    }

    @GetMapping("/roles")
    public SaResult getRoles() {
        return SaResult.data(StpUtil.getRoleList());
    }

    @GetMapping("/permissions")
    public SaResult getPermissions() {
        return SaResult.data(StpUtil.getPermissionList());
    }
}
