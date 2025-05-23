package com.zqqiliyc.domain.entity;

import com.zqqiliyc.utils.SnowFlakeUtils;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 基础实体
 * @author qili
 * @date 2025-03-22
 */
@Data
public abstract class BaseEntity implements Entity {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @io.mybatis.provider.Entity.Column(value = "id", id = true, genId = SnowFlakeUtils.class, updatable = false, remark = "主键")
    private Long id;
    /**
     * 创建时间
     */
    @io.mybatis.provider.Entity.Column(value = "create_time", insertable = false, updatable = false, remark = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @io.mybatis.provider.Entity.Column(value = "update_time", insertable = false, updatable = false, remark = "更新时间")
    private LocalDateTime updateTime;
}
