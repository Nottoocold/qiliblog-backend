package com.zqqiliyc.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.zqqiliyc.domain.entity.SysRole;
import com.zqqiliyc.domain.entity.SysUserRole;
import com.zqqiliyc.repository.mapper.SysRoleMapper;
import com.zqqiliyc.service.ISysRoleService;
import com.zqqiliyc.service.ISysUserRoleService;
import com.zqqiliyc.service.base.AbstractDeleteHardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author qili
 * @date 2025-04-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysRoleService extends AbstractDeleteHardService<SysRole, Long, SysRoleMapper> implements ISysRoleService {
    private final ISysUserRoleService userRoleService;

    /**
     * 根据角色码查询角色
     *
     * @param code 角色码
     * @return 角色
     */
    @Override
    public Optional<SysRole> findByCode(String code) {
        return wrapper().eq(SysRole::getCode, code).one();
    }

    /**
     * 根据用户ID查询所拥有的角色
     *
     * @param userId 用户ID
     * @return 角色
     */
    @Override
    public List<SysRole> findByUserId(Long userId) {
        List<SysUserRole> userRoles = userRoleService.findByUserId(userId);
        if (CollUtil.isEmpty(userRoles)) {
            return Collections.emptyList();
        }
        List<Long> ids = userRoles.stream().map(SysUserRole::getRoleId).toList();
        return findByFieldList(SysRole::getId, ids);
    }
}
