package com.example.satoken.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
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
}
