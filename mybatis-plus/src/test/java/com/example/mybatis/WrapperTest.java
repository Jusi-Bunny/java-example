package com.example.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.mybatis.entity.User;
import com.example.mybatis.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WrapperTest {

    @Autowired
    private UserService userService;

    @Test
    public void testQueryWrapper() {
        // 查询年龄大于20的
        List<User> userList = userService.list(new QueryWrapper<User>().gt("age", 20));
        userList.forEach(System.out::println);
    }

    @Test
    public void testLambdaQueryWrapper() {
        // 获取年龄大于20的
        List<User> userList = userService.list(new QueryWrapper<User>().lambda().gt(User::getAge, 20));
        userList.forEach(System.out::println);

        LambdaQueryWrapper<User> gt = new LambdaQueryWrapper<>(User.class).gt(User::getAge, 20);

    }

    @Test
    public void testWrappers() {
        List<User> userList = userService.list(Wrappers.lambdaQuery(User.class).gt(User::getAge, 20));
        userList.forEach(System.out::println);
    }

    @Test
    public void testLambdaQueryChainWrapper() {
        LambdaQueryChainWrapper<User> userLambdaQueryChainWrapper = userService.lambdaQuery();
        List<User> userList = userLambdaQueryChainWrapper.gt(User::getAge, 20).list();
        userList.forEach(System.out::println);
    }

    @Test
    public void testLogicDelete() {
        User user = userService.getById(1L);
        System.out.println(user);
        userService.removeById(1L);
    }

    @Test
    public void testIdWorker() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(IdWorker.getId());
            Thread.sleep(2000);
            // System.out.println(IdWorker.get32UUID());
            Thread.sleep(2000);
        }
    }
}
