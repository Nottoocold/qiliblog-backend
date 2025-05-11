package com.zqqiliyc.mapper;

import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.mapper.base.Dao;
import com.zqqiliyc.mapper.base.IDeleteMapper;
import io.mybatis.mapper.logical.LogicalMapper;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface SysUserMapper extends LogicalMapper<SysUser, Long>, IDeleteMapper<SysUser>, Dao {

}
