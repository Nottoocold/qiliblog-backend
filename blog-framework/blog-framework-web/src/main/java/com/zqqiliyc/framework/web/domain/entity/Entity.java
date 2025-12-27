package com.zqqiliyc.framework.web.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体接口
 * @author qili
 * @date 2025-03-22
 */
public interface Entity extends Serializable {

    int DEL_FLAG_NORMAL = 0;

    int DEL_FLAG_DELETED = 1;

    Long getId();

    void setId(Long id);

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);
}
