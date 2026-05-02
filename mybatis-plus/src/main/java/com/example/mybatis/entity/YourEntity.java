package com.example.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
@KeySequence("seq_user_id")
public class YourEntity {

    @TableId(value = "ID_STR", type = IdType.INPUT)
    private String idStr;

    // 其他字段和方法...
}