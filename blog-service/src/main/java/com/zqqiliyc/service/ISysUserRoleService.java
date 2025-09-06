package com.zqqiliyc.service;

import com.zqqiliyc.domain.entity.SysUserRole;
import com.zqqiliyc.service.base.IBaseService;

import java.util.List;
import java.util.Optional;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface ISysUserRoleService extends IBaseService<SysUserRole, Long> {

    /**
     * 根据用户ID和角色ID查询
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 用户角色信息
     */
    Optional<SysUserRole> findOne(Long userId, Long roleId);

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return 用户角色信息列表
     */
    List<SysUserRole> findByUserId(Long userId);
}
