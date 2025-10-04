package com.zqqiliyc.biz.core.repository.mapper;

import com.zqqiliyc.biz.core.entity.SysRole;
import com.zqqiliyc.biz.core.repository.Dao;
import com.zqqiliyc.biz.core.repository.IDeleteMapper;
import com.zqqiliyc.biz.core.repository.config.LogicalOptimizeSqlWrapper;
import io.mybatis.mapper.logical.LogicalMapper;
import io.mybatis.provider.SqlWrapper;

/**
 * @author qili
 * @date 2025-04-06
 */
@SqlWrapper(LogicalOptimizeSqlWrapper.class)
public interface SysRoleMapper extends LogicalMapper<SysRole, Long>, IDeleteMapper<SysRole>, Dao {

}
