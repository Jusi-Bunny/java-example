package com.example.satoken.service;

import org.springframework.stereotype.Service;

/**
 * @author Jusi
 * @date 2026/8/10
 */
public interface AuthService {
    Long authenticate(String username, String rawPassword);
}
