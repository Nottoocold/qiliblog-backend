package com.zqqiliyc.mapper;

import com.zqqiliyc.common.IDeleteMapper;
import com.zqqiliyc.domain.entity.SysUser;
import io.mybatis.mapper.logical.LogicalMapper;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface SysUserMapper extends LogicalMapper<SysUser, Long>, IDeleteMapper<SysUser> {

}
