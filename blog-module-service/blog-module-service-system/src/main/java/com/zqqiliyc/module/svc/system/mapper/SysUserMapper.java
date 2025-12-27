package com.zqqiliyc.module.svc.system.mapper;

import com.zqqiliyc.framework.web.domain.entity.SysUser;
import com.zqqiliyc.framework.web.mapper.Dao;
import com.zqqiliyc.framework.web.mapper.IDeleteMapper;
import com.zqqiliyc.framework.web.mybatis.LogicalOptimizeSqlWrapper;
import io.mybatis.mapper.logical.LogicalMapper;
import io.mybatis.provider.SqlWrapper;

/**
 * @author qili
 * @date 2025-04-06
 */
@SqlWrapper(LogicalOptimizeSqlWrapper.class)
public interface SysUserMapper extends LogicalMapper<SysUser, Long>, IDeleteMapper<SysUser>, Dao {

}
