package com.example.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectUserById(Long id);
}
