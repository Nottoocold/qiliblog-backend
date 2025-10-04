package com.zqqiliyc.biz.core.dto.priv;

import com.zqqiliyc.biz.core.dto.CreateDto;
import com.zqqiliyc.biz.core.entity.SysUserRole;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-06
 */
@Getter
@Setter
public class SysUserRoleCreateDto implements CreateDto<SysUserRole> {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;

    @Override
    public SysUserRole toEntity() {
        return new SysUserRole(getUserId(), getRoleId());
    }
}
