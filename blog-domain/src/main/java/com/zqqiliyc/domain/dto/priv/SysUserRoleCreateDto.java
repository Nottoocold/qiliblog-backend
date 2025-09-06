package com.zqqiliyc.domain.dto.priv;

import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.entity.SysUserRole;
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
