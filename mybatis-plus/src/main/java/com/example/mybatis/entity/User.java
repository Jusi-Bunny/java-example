package com.example.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.mybatis.enums.GenderEnum;
import lombok.Data;

@Data
@TableName("user")
public class User {

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
