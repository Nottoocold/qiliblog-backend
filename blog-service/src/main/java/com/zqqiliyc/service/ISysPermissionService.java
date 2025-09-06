package com.zqqiliyc.service;

import com.zqqiliyc.domain.entity.SysPermission;
import com.zqqiliyc.service.base.IBaseService;

import java.util.Optional;
import java.util.Set;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface ISysPermissionService extends IBaseService<SysPermission, Long> {

    /**
     * 根据权限编码查询权限
     *
     * @param code 权限编码
     * @return 权限
     */
    Optional<SysPermission> findByCode(String code);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> findByUserId(Long userId);
}
