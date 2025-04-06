package com.zqqiliyc.domain.entity;

import io.mybatis.provider.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户角色关联
 * @author qili
 * @date 2025-03-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity.Table("sys_user_role")
public class SysUserRole extends BaseEntity {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
