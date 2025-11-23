package com.zqqiliyc.module.biz.repository.mapper;

import com.zqqiliyc.module.biz.config.mybatis.LogicalOptimizeSqlWrapper;
import com.zqqiliyc.module.biz.entity.SysUser;
import com.zqqiliyc.module.biz.repository.Dao;
import com.zqqiliyc.module.biz.repository.IDeleteMapper;
import io.mybatis.mapper.logical.LogicalMapper;
import io.mybatis.provider.SqlWrapper;

/**
 * @author qili
 * @date 2025-04-06
 */
@SqlWrapper(LogicalOptimizeSqlWrapper.class)
public interface SysUserMapper extends LogicalMapper<SysUser, Long>, IDeleteMapper<SysUser>, Dao {

}
