package com.example.satoken.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jusi
 * @date 2026/8/10
 */
@Slf4j
@SpringBootTest
class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    void selectRoleCodeListByUserId() {
        List<String> roleCodeList = sysRoleMapper.selectRoleCodeListByUserId(10001L);
        assertThat(roleCodeList)
                .containsExactlyInAnyOrder("admin", "super-admin");
        log.info("roleCodeList = {}", roleCodeList);
    }
}