package com.example.satoken.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.satoken.request.SessionRequest;
import com.example.satoken.response.SessionResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jusi
 * @date 2026/8/14
 */
@RequestMapping("/session")
@RestController
public class SessionController {

    @SaCheckRole("admin")
    @PostMapping("/users/{userId}/kickout")
    public SaResult kickout(@PathVariable Long userId) {
        // 根据 userId 踢出该账号
        StpUtil.kickout(userId);
        // 返回操作成功
        return SaResult.ok();
    }

    @SaCheckRole("admin")
    @PostMapping("/users/kickoutByTokenValue")
    public SaResult kickoutByTokenValue(@RequestParam String tokenValue) {
        // 根据 tokenValue 踢出指定 Token
        StpUtil.kickoutByTokenValue(tokenValue);
        // 返回操作成功
        return SaResult.ok();
    }

    @SaCheckRole("admin")
    @PostMapping("/users/{userId}/devices/{deviceType}/kickout")
    public SaResult kickout(@PathVariable Long userId, @PathVariable String deviceType) {
        // 踢出该账号指定设备类型下的所有 Token
        StpUtil.kickout(userId, deviceType);
        // 返回操作成功
        return SaResult.ok();
    }

    @PostMapping("/demo-state")
    public SaResult setDemoState(@Valid @RequestBody SessionRequest request) {
        SaSession accountSession = StpUtil.getSession();
        SaSession tokenSession = StpUtil.getTokenSession();
        accountSession.set("sharedLabel", request.getSharedLabel());
        tokenSession.set("localStep", request.getLocalStep());
        return SaResult.ok();
    }

    @GetMapping("/demo-state")
    public SaResult getDemoState() {
        SessionResponse sessionResponse = new SessionResponse();
        SaSession accountSession = StpUtil.getSession();
        SaSession tokenSession = StpUtil.getTokenSession();
        String sharedLabel = accountSession.get("sharedLabel", "");
        String localStep = tokenSession.get("localStep", "");
        sessionResponse.setSharedLabel(sharedLabel);
        sessionResponse.setLocalStep(localStep);
        return SaResult.data(sessionResponse);
    }
}
