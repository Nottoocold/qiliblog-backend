package com.zqqiliyc.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.zqqiliyc.domain.entity.SysPermission;
import com.zqqiliyc.domain.entity.SysRolePriv;
import com.zqqiliyc.domain.entity.SysUserRole;
import com.zqqiliyc.repository.mapper.SysPermissionMapper;
import com.zqqiliyc.service.ISysPermissionService;
import com.zqqiliyc.service.ISysRolePrivService;
import com.zqqiliyc.service.ISysUserRoleService;
import com.zqqiliyc.service.base.AbstractBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qili
 * @date 2025-04-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysPermissionService extends AbstractBaseService<SysPermission, Long, SysPermissionMapper> implements ISysPermissionService {
    private final ISysUserRoleService userRoleService;
    private final ISysRolePrivService rolePrivService;

    /**
     * 根据权限编码查询权限
     *
     * @param code 权限编码
     * @return 权限
     */
    @Override
    public Optional<SysPermission> findByCode(String code) {
        return wrapper().eq(SysPermission::getCode, code).one();
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> findByUserId(Long userId) {
        List<SysUserRole> userRoles = userRoleService.findByUserId(userId);
        if (CollUtil.isEmpty(userRoles)) {
            return Collections.emptySet();
        }
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).toList();
        List<SysRolePriv> sysRolePrivs = rolePrivService.findByRoleIdList(roleIds);
        if (CollUtil.isEmpty(sysRolePrivs)) {
            return Collections.emptySet();
        }
        Set<Long> privIds = sysRolePrivs.stream().map(SysRolePriv::getPrivId).collect(Collectors.toUnmodifiableSet());
        List<SysPermission> permissionList = findByFieldList(SysPermission::getId, privIds);
        return permissionList.stream().map(SysPermission::getCode).collect(Collectors.toUnmodifiableSet());
    }
}
