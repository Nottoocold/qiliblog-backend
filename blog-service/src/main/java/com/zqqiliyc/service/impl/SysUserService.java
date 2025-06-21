package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.repository.mapper.SysUserMapper;
import com.zqqiliyc.service.ISysUserService;
import com.zqqiliyc.service.base.AbstractDeleteHardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserService extends AbstractDeleteHardService<SysUser, Long, SysUserMapper> implements ISysUserService {

}
