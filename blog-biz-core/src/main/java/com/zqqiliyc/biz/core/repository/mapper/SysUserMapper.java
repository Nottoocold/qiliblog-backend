package com.zqqiliyc.biz.core.repository.mapper;

import com.zqqiliyc.biz.core.config.mybatis.LogicalOptimizeSqlWrapper;
import com.zqqiliyc.biz.core.entity.SysUser;
import com.zqqiliyc.biz.core.repository.Dao;
import com.zqqiliyc.biz.core.repository.IDeleteMapper;
import io.mybatis.mapper.logical.LogicalMapper;
import io.mybatis.provider.SqlWrapper;

/**
 * @author qili
 * @date 2025-04-06
 */
@SqlWrapper(LogicalOptimizeSqlWrapper.class)
public interface SysUserMapper extends LogicalMapper<SysUser, Long>, IDeleteMapper<SysUser>, Dao {

}
