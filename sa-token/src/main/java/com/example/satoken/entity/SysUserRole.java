package com.example.satoken.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.satoken.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 角色 ID
     */
    private Long roleId;
}
