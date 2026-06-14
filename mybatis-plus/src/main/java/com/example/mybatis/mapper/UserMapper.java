package com.example.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mybatis.entity.User;
import com.example.mybatis.enums.GenderEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectUserById(Long id);

    List<User> selectByGender(@Param("gender") GenderEnum gender);

    int insertUser(User user);

    // int insertUser(@Param("et") User user);
}
