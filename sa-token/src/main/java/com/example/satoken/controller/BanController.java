package com.example.satoken.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.satoken.request.BanLevelRequest;
import com.example.satoken.response.BanLevelStatusResponse;
import com.example.satoken.response.BanStatusResponse;
import jakarta.validation.Valid;
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

    // 封禁导出 60 秒
    @PostMapping("/users/{userId}/services/demo-export")
    public SaResult banUserExport(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        StpUtil.disable(userId, "demo-export", 60);
        return SaResult.ok("账号已封禁");
    }

    // 查询导出封禁
    @GetMapping("/users/{userId}/services/demo-export")
    public SaResult getUserBanStatusExport(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        BanStatusResponse response = new BanStatusResponse();
        response.setDisabled(StpUtil.isDisable(userId, "demo-export"));
        response.setRemainingSeconds(StpUtil.getDisableTime(userId, "demo-export"));
        return SaResult.data(response);
    }

    // 解除导出封禁
    @DeleteMapping("/users/{userId}/services/demo-export")
    public SaResult unbanUserExport(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        StpUtil.untieDisable(userId, "demo-export");
        return SaResult.ok("账号已解封");
    }

    // 封禁删除 60 秒
    @PostMapping("/users/{userId}/services/demo-delete")
    public SaResult banUserDelete(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        StpUtil.disable(userId, "demo-delete", 60);
        return SaResult.ok("账号已封禁");
    }

    // 查询删除封禁
    @GetMapping("/users/{userId}/services/demo-delete")
    public SaResult getUserBanStatusDelete(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        BanStatusResponse response = new BanStatusResponse();
        response.setDisabled(StpUtil.isDisable(userId, "demo-delete"));
        response.setRemainingSeconds(StpUtil.getDisableTime(userId, "demo-delete"));
        return SaResult.data(response);
    }

    // 解除删除封禁
    @DeleteMapping("/users/{userId}/services/demo-delete")
    public SaResult unbanUserDelete(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        StpUtil.untieDisable(userId, "demo-delete");
        return SaResult.ok("账号已解封");
    }

    @PostMapping("/users/{userId}/services/demo-risk")
    public SaResult banUserRisk(@PathVariable String userId, @Valid @RequestBody BanLevelRequest request) {
        StpUtil.checkRole("admin");
        StpUtil.disableLevel(userId, "demo-risk", request.getLevel(), 60L);
        return SaResult.ok("账号已封禁");
    }

    @GetMapping("/users/{userId}/services/demo-risk")
    public SaResult getUserBanStatusRisk(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        BanLevelStatusResponse response = new BanLevelStatusResponse();
        response.setLevel(StpUtil.getDisableLevel(userId, "demo-risk"));
        response.setRemainingSeconds(StpUtil.getDisableTime(userId, "demo-risk"));
        return SaResult.data(response);
    }

    @DeleteMapping("/users/{userId}/services/demo-risk")
    public SaResult unbanUserRisk(@PathVariable String userId) {
        StpUtil.checkRole("admin");
        StpUtil.untieDisable(userId, "demo-risk");
        return SaResult.ok("账号已解封");
    }
}
