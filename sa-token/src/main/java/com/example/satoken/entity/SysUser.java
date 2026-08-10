package com.example.satoken.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.satoken.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;
}
