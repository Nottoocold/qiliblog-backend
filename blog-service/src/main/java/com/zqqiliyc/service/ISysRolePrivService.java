package com.zqqiliyc.service;

import com.zqqiliyc.domain.entity.SysRolePriv;
import com.zqqiliyc.service.base.IBaseService;

import java.util.Optional;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface ISysRolePrivService extends IBaseService<SysRolePriv, Long> {

    /**
     * 根据角色id和权限id查询
     *
     * @param roleId 角色id
     * @param privId 权限id
     * @return 角色权限
     */
    Optional<SysRolePriv> findOne(Long roleId, Long privId);
}
