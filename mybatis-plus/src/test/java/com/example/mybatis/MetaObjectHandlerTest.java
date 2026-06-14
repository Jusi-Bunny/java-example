package com.example.mybatis;

import com.example.mybatis.entity.User;
import com.example.mybatis.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MetaObjectHandlerTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        User user = new User();
        user.setName("Joi");
        user.setAge(18);
        user.setEmail("joi@example.com");
        int result = userMapper.insert(user);
        System.out.println("result = " + result);
    }

    @Test
    public void testCustomerInsert() {
        User user = new User();
        user.setName("Joi");
        user.setAge(18);
        user.setEmail("joi@example.com");
        int result = userMapper.insertUser(user);
        System.out.println("result = " + result);
    }
}
