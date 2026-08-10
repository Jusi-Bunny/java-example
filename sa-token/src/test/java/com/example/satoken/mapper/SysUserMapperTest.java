package com.example.satoken.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.satoken.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jusi
 * @date 2026/8/10
 */
@Slf4j
@SpringBootTest
class SysUserMapperTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    void selectByUsername() {
        String username = "admin";
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>(SysUser.class)
                .eq(SysUser::getUsername, username)
                .eq(SysUser::getStatus, 1);

        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        assertThat(sysUser).isNotNull();
        assertThat(sysUser.getId()).isEqualTo(10001L);
        assertThat(sysUser.getUsername()).isEqualTo("admin");
        log.info("sysUser = {}", sysUser);
    }
}