package com.zqqiliyc.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体接口
 * @author qili
 * @date 2025-03-22
 */
public interface Entity extends Serializable {

    long getId();

    void setId(long id);

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);

    long getDelFlag();

    void setDelFlag(long delFlag);

    /**
     * 是否可以删除
     *
     * @return true 可以删除，false 不可以删除
     */
    default boolean canDelete() {
        return getDelFlag() == 0;
    }

    /**
     * 是否可以更新
     *
     * @return true 可以更新，false 不可以更新
     */
    default boolean canUpdate() {
        return getDelFlag() == 0;
    }
}
