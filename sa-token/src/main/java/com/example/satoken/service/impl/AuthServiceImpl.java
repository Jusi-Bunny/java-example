package com.example.satoken.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.example.satoken.service.AuthService;

/**
 * @author Jusi
 * @date 2026/8/10
 */
public class AuthServiceImpl implements AuthService {

    @Override
    public Long authenticate(String username, String rawPassword) {

        String encodedPassword = "encodedPassword";
        boolean checkpw = BCrypt.checkpw(rawPassword, encodedPassword);
        return 0L;
    }
}
