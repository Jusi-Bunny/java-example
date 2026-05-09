package com.example.junit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Test
    void testAddUser() {
        System.out.println("JUnit 5 test");
    }
}