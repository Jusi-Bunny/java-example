package com.example.mybatis;

import com.example.mybatis.entity.User;
import com.example.mybatis.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class MyBatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectList() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void testSelectById() {
        System.out.println(("----- selectById method test ------"));
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    @Test
    public void testInsert() {
        System.out.println(("----- insert method test ------"));
        User user = new User();
        user.setName("Joi");
        user.setAge(18);
        user.setEmail("joi@example.com");
        int result = userMapper.insert(user);
        System.out.println("result = " + result);
    }

    @Test
    public void testUpdateById() {
        System.out.println(("----- updateById method test ------"));
        User user = userMapper.selectById(1L);
        user.setName("XiaoMing");
        // 修改
        userMapper.updateById(user);

    }

    @Test
    public void testDeleteById() {
        System.out.println(("----- deleteById method test ------"));
        int result = userMapper.deleteById(1L);
        System.out.println("result = " + result);
    }
}
