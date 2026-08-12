package com.example.satoken.service;

/**
 * @author Jusi
 * @date 2026/8/10
 */
public interface AuthService {
    Long authenticate(String username, String rawPassword);
}
