package com.example.mybatis.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建人 ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 修改人 ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
