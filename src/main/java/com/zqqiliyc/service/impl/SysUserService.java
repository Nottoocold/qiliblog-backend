package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.mapper.SysUserMapper;
import com.zqqiliyc.service.ISysUserService;
import io.mybatis.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * @author qili
 * @date 2025-04-06
 */
@Service
public class SysUserService extends AbstractService<SysUser, Long, SysUserMapper> implements ISysUserService {

}
