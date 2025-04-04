package com.zqqiliyc.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * 基础实体
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
public abstract class BaseEntity implements Entity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return new StringJoiner(", ", BaseEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("createTime=" + createTime)
                .add("updateTime=" + updateTime)
                .toString();
    }
}
