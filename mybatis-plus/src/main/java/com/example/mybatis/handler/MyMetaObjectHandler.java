package com.example.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        Long userId = getCurrentUserId();

        this.strictInsertFill(metaObject, "createdBy", Long.class, userId);
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);

        this.strictInsertFill(metaObject, "updatedBy", Long.class, userId);
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        Long userId = getCurrentUserId();

        this.strictUpdateFill(metaObject, "updatedBy", Long.class, userId);
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, now);
    }

    private Long getCurrentUserId() {
        // 实际项目中可以从登录上下文、Token、ThreadLocal、MDC 等获取
        // 注意：如果是异步线程、定时任务、SSE 回调线程，不一定能直接取到登录用户
        return 1L;
    }
}