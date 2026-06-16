package com.example.okhttp.client.user;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String name;
    private Integer age;
}