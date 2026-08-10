package com.example.satoken.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.satoken.request.LoginRequest;
import com.example.satoken.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jusi
 * @date 2026/8/10
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public SaResult login(@RequestBody LoginRequest request) {
        Long userId = authService.authenticate(request.getUsername(), request.getPassword());
        return SaResult.data(userId);
    }
}
