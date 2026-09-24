package com.example.springcache.service.impl;

import com.example.springcache.entity.User;
import com.example.springcache.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Jusi
 * @date 2026/9/24
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Override
    @Cacheable(cacheNames = "users", key = "#id", condition = "#id > 0")
    public User getUserById(Long id) {
        User user = new User();
        user.setId(id);
        user.setName(UUID.randomUUID().toString().replace("-", ""));
        return user;
    }
}
