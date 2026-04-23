package com.example.mybatis.common.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建者标识
     */
    private LocalDateTime createdAt;

    /**
     * 修改者标识
     */
    private String updatedBy;

    /**
     * 修改时间
     */
    private LocalDateTime updatedAt;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    private Integer isDeleted;
}
