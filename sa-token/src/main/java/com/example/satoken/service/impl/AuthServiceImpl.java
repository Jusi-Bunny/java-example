package com.example.satoken.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.satoken.common.exception.AccountDisabledException;
import com.example.satoken.common.exception.LoginException;
import com.example.satoken.entity.SysUser;
import com.example.satoken.mapper.SysUserMapper;
import com.example.satoken.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Jusi
 * @date 2026/8/10
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Long authenticate(String username, String rawPassword) {

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>(SysUser.class)
                .eq(SysUser::getUsername, username);

        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);

        // if (sysUser == null || !BCrypt.checkpw(rawPassword, sysUser.getPassword())) {
        //     throw new LoginException("用户名或密码错误");
        // }

        if (sysUser == null || !passwordEncoder.matches(rawPassword, sysUser.getPassword())) {
            throw new LoginException("用户名或密码错误");
        }

        if (sysUser.getStatus() == 0) {
            throw new AccountDisabledException("当前用户被禁用");
        }

        return sysUser.getId();
    }
}
