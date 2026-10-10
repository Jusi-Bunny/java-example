package com.example.satoken.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.satoken.request.SafeVerifyRequest;
import com.example.satoken.response.SafeStatusResponse;
import com.example.satoken.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jusi
 * @date 2026/10/10
 */
@RestController
@RequestMapping("/safe")
@RequiredArgsConstructor
public class SafeController {

    private final AuthService authService;

    // 校验当前账号密码，成功后开启认证
    @PostMapping("/verify")
    public ResponseEntity<SaResult> verify(@Valid @RequestBody SafeVerifyRequest request) {
        if (!authService.verifyPassword(StpUtil.getLoginIdAsLong(), request.getPassword())) {
            return ResponseEntity.status(403).body(SaResult.code(403).setMsg("密码错误"));
        }
        StpUtil.openSafe("demo-export", 30);
        return ResponseEntity.ok(SaResult.ok());
    }

    // 查询认证状态
    @GetMapping("/status")
    public SaResult status() {
        SafeStatusResponse response = new SafeStatusResponse();
        response.setSafe(StpUtil.isSafe("demo-export"));
        response.setRemainingSeconds(StpUtil.getSafeTime("demo-export"));
        return SaResult.data(response);
    }

    // 模拟导出成功
    @PostMapping("/demo-export")
    public SaResult demoExport() {
        StpUtil.checkSafe("demo-export");
        return SaResult.ok("模拟导出成功");
    }

    // 关闭认证窗口
    @PostMapping("/close")
    public SaResult close() {
        StpUtil.closeSafe("demo-export");
        return SaResult.ok();
    }

    @PostMapping("/verify-delete")
    public ResponseEntity<SaResult> verifyDelete(@Valid @RequestBody SafeVerifyRequest request) {
        if (!authService.verifyPassword(StpUtil.getLoginIdAsLong(), request.getPassword())) {
            return ResponseEntity.status(403).body(SaResult.code(403).setMsg("密码错误"));
        }
        StpUtil.openSafe("demo-delete", 30);
        return ResponseEntity.ok(SaResult.ok());
    }

    @GetMapping("/delete-status")
    public SaResult deleteStatus() {
        SafeStatusResponse response = new SafeStatusResponse();
        response.setSafe(StpUtil.isSafe("demo-delete"));
        response.setRemainingSeconds(StpUtil.getSafeTime("demo-delete"));
        return SaResult.data(response);
    }

    @PostMapping("/demo-delete")
    public SaResult demoDelete() {
        StpUtil.checkSafe("demo-delete");
        return SaResult.ok("模拟删除成功");
    }

    @PostMapping("/close-delete")
    public SaResult closeDelete() {
        StpUtil.closeSafe("demo-delete");
        return SaResult.ok();
    }
}
