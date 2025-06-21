package com.zqqiliyc.repository.mapper;

import com.zqqiliyc.domain.entity.SysRole;
import com.zqqiliyc.repository.Dao;
import com.zqqiliyc.repository.IDeleteMapper;
import com.zqqiliyc.repository.config.LogicalOptimizeSqlWrapper;
import io.mybatis.mapper.logical.LogicalMapper;
import io.mybatis.provider.SqlWrapper;

/**
 * @author qili
 * @date 2025-04-06
 */
@SqlWrapper(LogicalOptimizeSqlWrapper.class)
public interface SysRoleMapper extends LogicalMapper<SysRole, Long>, IDeleteMapper<SysRole>, Dao {

}
