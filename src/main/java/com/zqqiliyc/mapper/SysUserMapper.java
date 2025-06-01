package com.zqqiliyc.mapper;

import com.zqqiliyc.config.LogicalOptimizeSqlWrapper;
import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.mapper.base.Dao;
import com.zqqiliyc.mapper.base.IDeleteMapper;
import io.mybatis.mapper.logical.LogicalMapper;
import io.mybatis.provider.SqlWrapper;

/**
 * @author qili
 * @date 2025-04-06
 */
@SqlWrapper(LogicalOptimizeSqlWrapper.class)
public interface SysUserMapper extends LogicalMapper<SysUser, Long>, IDeleteMapper<SysUser>, Dao {

}
