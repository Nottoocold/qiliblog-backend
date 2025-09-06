package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysRolePriv;
import com.zqqiliyc.repository.mapper.SysRolePrivMapper;
import com.zqqiliyc.service.ISysRolePrivService;
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
public class SysRolePrivService extends AbstractBaseService<SysRolePriv, Long, SysRolePrivMapper> implements ISysRolePrivService {

    /**
     * 根据角色id和权限id查询
     *
     * @param roleId 角色id
     * @param privId 权限id
     * @return 角色权限
     */
    @Override
    public Optional<SysRolePriv> findOne(Long roleId, Long privId) {
        return wrapper().eq(SysRolePriv::getRoleId, roleId).eq(SysRolePriv::getPrivId, privId).one();
    }
}
