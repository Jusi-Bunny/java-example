package com.example.satoken.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.satoken.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@TableName("sys_permission")
public class SysPermission extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限类型：1-菜单，2-按钮，3-接口，4-数据
     */
    private Integer permissionType;

    /**
     * 父权限 ID
     */
    private Long parentId;

    /**
     * 前端路由路径或接口路径
     */
    private String path;

    /**
     * 前端组件路径
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}
