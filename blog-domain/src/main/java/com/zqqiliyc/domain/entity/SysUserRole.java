package com.zqqiliyc.domain.entity;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import io.mybatis.provider.Entity;
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

    public SysUserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public void setStartTime(LocalDateTime startTime) {
        LocalDateTime s = ObjectUtil.defaultIfNull(startTime, LocalDateTime.now());
        this.startTime = LocalDateTimeUtil.beginOfDay(s);
    }

    public void setEndTime(LocalDateTime endTime) {
        LocalDateTime e = ObjectUtil.defaultIfNull(endTime, LocalDateTime.now());
        this.endTime = LocalDateTimeUtil.endOfDay(e);
    }
}
