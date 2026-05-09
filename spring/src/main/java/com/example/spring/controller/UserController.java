package com.example.spring.controller;

import com.example.spring.request.UserCreateReq;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/save")
    public String saveUser(@ModelAttribute UserCreateReq user) {
        System.out.println(user);
        return "success";
    }
}
