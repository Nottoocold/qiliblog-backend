package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysRolePriv;
import com.zqqiliyc.mapper.SysRolePrivMapper;
import com.zqqiliyc.service.ISysRolePrivService;
import com.zqqiliyc.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRolePrivService extends AbstractBaseService<SysRolePriv, Long, SysRolePrivMapper> implements ISysRolePrivService {

}
