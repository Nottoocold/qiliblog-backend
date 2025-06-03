package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysPermission;
import com.zqqiliyc.mapper.SysPermissionMapper;
import com.zqqiliyc.service.ISysPermissionService;
import com.zqqiliyc.service.base.AbstractExtendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysPermissionService extends AbstractExtendService<SysPermission, Long, SysPermissionMapper> implements ISysPermissionService {

}
