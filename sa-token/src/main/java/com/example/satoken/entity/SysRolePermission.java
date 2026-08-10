package com.example.satoken.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.satoken.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@TableName("sys_role_permission")
public class SysRolePermission extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 权限 ID
     */
    private Long permissionId;
}
