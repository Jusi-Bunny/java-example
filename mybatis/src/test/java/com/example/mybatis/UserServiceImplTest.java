package com.example.mybatis;

import com.example.mybatis.entity.User;
import com.example.mybatis.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void testSelect() {
        // 查找单条数据
        User user = userService.getById(1L);
        System.out.println(user);

        // 查找所有数据
        List<User> list = userService.list();
        list.forEach(System.out::println);
    }

    @Test
    public void testSaveOrUpdate() {
        User user1 = userService.getById(2L);
        user1.setName("ZhangSan");

        User user2 = new User();
        user2.setName("LiSi");
        user2.setAge(27);
        user2.setEmail("lisi@baomidou.com");
        // 如果没有，新增；如果有，修改
        userService.saveOrUpdate(user1);
        userService.saveOrUpdate(user2);
    }

    @Test
    public void testSaveBatch() {
        User user1 = new User();
        user1.setName("WangWu");
        user1.setAge(49);
        user1.setEmail("wangwu@baomidou.com");

        User user2 = new User();
        user2.setName("ZhaoLiu");
        user2.setAge(29);
        user2.setEmail("zhaoliu@baomidou.com");

        List<User> users = List.of(user1, user2);
        // 批量添加
        userService.saveBatch(users);
    }
}
