package com.example.springcache.controller;

import com.example.springcache.entity.User;
import com.example.springcache.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Jusi
 * @date 2026/9/23
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("查询用户：{}", id);
        return userService.getUserById(id);
    }
}
