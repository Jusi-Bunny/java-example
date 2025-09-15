package com.example.knife4j.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "示例接口")
@RestController
@RequestMapping("/example")
public class ExampleController {

    @GetMapping("/hello")
    public String hello() {
        log.info("Hello, World!");
        return "Hello, World!";
    }
}
