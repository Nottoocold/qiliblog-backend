package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysPermission;
import com.zqqiliyc.repository.mapper.SysPermissionMapper;
import com.zqqiliyc.service.ISysPermissionService;
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
public class SysPermissionService extends AbstractBaseService<SysPermission, Long, SysPermissionMapper> implements ISysPermissionService {

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
}
