package com.example.junit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

    @BeforeEach
    void beforeEach() {
        System.out.println("每个测试方法执行前执行");
    }

    @AfterEach
    void afterEach() {
        System.out.println("每个测试方法执行后执行");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("所有测试方法执行前执行一次");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("所有测试方法执行后执行一次");
    }

    @Test
    void testAddUser() {
        System.out.println("JUnit 5 test");
    }
}