package com.zqqiliyc.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户角色关联
 * @author qili
 * @date 2025-03-22
 */
@Getter
@Setter
public class SysUserRole extends BaseEntity {
    /**
     * 用户id
     */
    private long userId;
    /**
     * 角色id
     */
    private long roleId;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
