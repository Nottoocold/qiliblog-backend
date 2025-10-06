package com.zqqiliyc.biz.core.dto.priv;

import com.zqqiliyc.biz.core.dto.CreateDTO;
import com.zqqiliyc.biz.core.entity.SysRolePriv;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-06
 */
@Getter
@Setter
public class SysRolePrivCreateDTO implements CreateDTO<SysRolePriv> {
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 权限ID
     */
    private Long privId;

    @Override
    public SysRolePriv toEntity() {
        return new SysRolePriv(getRoleId(), getPrivId());
    }
}
