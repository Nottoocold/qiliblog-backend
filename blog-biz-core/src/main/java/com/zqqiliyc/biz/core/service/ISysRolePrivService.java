package com.zqqiliyc.biz.core.service;

import com.zqqiliyc.biz.core.entity.SysRolePriv;
import com.zqqiliyc.biz.core.service.base.IBaseService;

import java.util.Collection;
import java.util.List;
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

    /**
     * 根据角色id查询
     *
     * @param roleId 角色id
     * @return 角色权限
     */
    List<SysRolePriv> findByRoleId(Long roleId);

    /**
     * 根据角色id列表查询
     *
     * @param roleIdList 角色id列表
     * @return 角色权限
     */
    List<SysRolePriv> findByRoleIdList(Collection<Long> roleIdList);
}
