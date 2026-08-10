package com.example.satoken.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jusi
 * @date 2026/8/10
 */
@Slf4j
@SpringBootTest
class SysPermissionMapperTest {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Test
    void selectPermissionCodeListByUserId() {
        List<String> permissionCodeList = sysPermissionMapper.selectPermissionCodeListByUserId(10001L);
        assertThat(permissionCodeList)
                .containsExactlyInAnyOrder(
                        "101", "user.add", "user.update", "user.get", "art.*"
                );
        log.info("permissionCodeList = {}", permissionCodeList);
    }
}