package com.example.satoken.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.satoken.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    // 根据 user_id 查询该用户的全部有效角色编码
    List<String> selectRoleCodeListByUserId(Long userId);
}
