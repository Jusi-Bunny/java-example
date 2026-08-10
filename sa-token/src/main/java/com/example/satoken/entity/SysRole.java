package com.example.satoken.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.satoken.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@TableName("sys_role")
public class SysRole extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}
