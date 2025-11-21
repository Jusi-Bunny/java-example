package com.example.knife4j.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
@Slf4j
@Tag(name = "示例接口")
@RestController
@RequestMapping("/example")
public class ExampleController {

    @Operation(summary = "Hello World")
    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
        log.info("Hello, World!");
        log.info("请求头: {}", request.getHeader("Authorization"));
        return "Hello, World!";
    }

    @Operation(summary = "Hello Java")
    @GetMapping("/helloJava")
    public String helloJava() {
        log.info("Hello, Java!");
        return "Hello, Java!";
    }
}
