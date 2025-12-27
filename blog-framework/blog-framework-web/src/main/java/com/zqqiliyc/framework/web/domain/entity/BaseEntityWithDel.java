package com.zqqiliyc.framework.web.domain.entity;

import io.mybatis.mapper.logical.LogicalColumn;
import io.mybatis.provider.Entity;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

/**
 * @author qili
 * @date 2025-03-26
 */
@Getter
@Setter
public abstract class BaseEntityWithDel extends BaseEntity {
    /**
     * 删除标志 0-未删除 非0-已删除
     */
    @LogicalColumn(delete = "1")
    @Entity.Column(value = "del_flag", jdbcType = JdbcType.INTEGER, insertable = false, updatable = false)
    private int delFlag;

    public BaseEntityWithDel() {
        this.delFlag = 0;
    }

    /**
     * 是否可以删除
     *
     * @return true 可以删除，false 不可以删除
     */
    public boolean canDelete() {
        return getDelFlag() == DEL_FLAG_NORMAL;
    }

    /**
     * 是否可以更新
     *
     * @return true 可以更新，false 不可以更新
     */
    public boolean canUpdate() {
        return getDelFlag() == DEL_FLAG_NORMAL;
    }
}
