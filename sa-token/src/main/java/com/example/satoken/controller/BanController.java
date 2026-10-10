package com.example.satoken.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.satoken.response.BanStatusResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jusi
 * @date 2026/10/10
 */
@RestController
@RequestMapping("/ban")
public class BanController {

    @PostMapping("/users/{userId}")
    public SaResult banUser(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        StpUtil.disable(userId, 60);
        return SaResult.ok("账号已封禁");
    }

    @GetMapping("/users/{userId}")
    public SaResult getUserBanStatus(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        BanStatusResponse response = new BanStatusResponse();
        response.setDisabled(StpUtil.isDisable(userId));
        response.setRemainingSeconds(StpUtil.getDisableTime(userId));
        return SaResult.data(response);
    }

    @DeleteMapping("/users/{userId}")
    public SaResult unbanUser(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        StpUtil.untieDisable(userId);
        return SaResult.ok("账号已解封");
    }

    @PostMapping("/users/{userId}/kickout")
    public SaResult kickoutUser(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        StpUtil.disable(userId, 60);
        StpUtil.kickout(userId);
        return SaResult.ok("账号已踢下线");
    }
}
