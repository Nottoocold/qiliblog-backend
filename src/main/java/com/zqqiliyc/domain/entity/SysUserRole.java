package com.zqqiliyc.domain.entity;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import io.mybatis.provider.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * 用户角色关联
 * @author qili
 * @date 2025-03-22
 */
@Getter
@EqualsAndHashCode(callSuper = true)
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
        setUserId(userId);
        setRoleId(roleId);
        setStartTime(null);
        setEndTime(getStartTime().plusDays(365));
    }

    public void setUserId(Long userId) {
        Assert.isTrue(userId != null, "userId不能为空");
        this.userId = userId;
    }

    public void setRoleId(Long roleId) {
        Assert.isTrue(roleId != null, "roleId不能为空");
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
