package com.example.satoken.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.satoken.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    // 根据 user_id 查询该用户通过角色获得的全部有效权限码
    List<String> selectPermissionCodeListByUserId(Long userId);
}
