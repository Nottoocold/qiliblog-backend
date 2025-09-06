package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysUserRole;
import com.zqqiliyc.repository.mapper.SysUserRoleMapper;
import com.zqqiliyc.service.ISysUserRoleService;
import com.zqqiliyc.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRoleService extends AbstractBaseService<SysUserRole, Long, SysUserRoleMapper> implements ISysUserRoleService {

    /**
     * 根据用户ID和角色ID查询
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 用户角色信息
     */
    @Override
    public Optional<SysUserRole> findOne(Long userId, Long roleId) {
        return wrapper().eq(SysUserRole::getUserId, userId).eq(SysUserRole::getRoleId, roleId).one();
    }
}
