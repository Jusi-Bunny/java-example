package com.example.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.mybatis.common.entity.BaseEntity;
import com.example.mybatis.enums.GenderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("user")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("age")
    private Integer age;

    @TableField("email")
    private String email;

    @TableLogic
    private Integer is_deleted;

    private GenderEnum gender;
}
