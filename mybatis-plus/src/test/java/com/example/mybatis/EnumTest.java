package com.example.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.mybatis.entity.User;
import com.example.mybatis.enums.GenderEnum;
import com.example.mybatis.mapper.UserMapper;
import com.example.mybatis.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EnumTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testEnumInsert() {
        User user = new User();
        user.setName("Joi");
        user.setAge(18);
        user.setGender(GenderEnum.FEMALE);
        userService.save(user);
    }

    @Test
    public void testEnumSelect() {
        User user = userService.getById(1L);
        System.out.println(user.getGender());
    }

    @Test
    public void testEnumWrapper() {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class).eq(User::getGender, GenderEnum.FEMALE);
        List<User> userList = userService.list(wrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void testEnumSelectByGender() {
        List<User> userList = userMapper.selectByGender(GenderEnum.FEMALE);
        userList.forEach(System.out::println);
    }

}
