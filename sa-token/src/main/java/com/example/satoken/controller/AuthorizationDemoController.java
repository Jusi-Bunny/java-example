package com.example.satoken.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jusi
 * @date 2026/8/12
 */
@Slf4j
@RequestMapping("/authorization")
@RestController
@RequiredArgsConstructor
public class AuthorizationDemoController {

    // 角色校验：必须具有指定角色才能进入该方法
    @GetMapping("/admin")
    @SaCheckRole("admin")
    public SaResult admin() {
        return SaResult.ok("管理员");
    }

    // 权限校验：必须具有指定权限才能进入该方法
    @PostMapping("/users")
    @SaCheckPermission("user.add")
    public SaResult users() {
        return SaResult.ok("用户添加");
    }

    // 权限校验：必须具有指定权限才能进入该方法
    @DeleteMapping("/users/{id}")
    @SaCheckPermission("user.delete")
    public SaResult deleteUsers(@PathVariable Long id) {
        log.info("id = {}", id);
        return SaResult.ok("用户删除");
    }
}
