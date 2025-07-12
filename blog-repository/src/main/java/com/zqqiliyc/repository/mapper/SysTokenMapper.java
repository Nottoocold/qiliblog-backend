package com.zqqiliyc.repository.mapper;

import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.repository.Dao;
import com.zqqiliyc.repository.config.LogicalOptimizeSqlWrapper;
import io.mybatis.mapper.BaseMapper;
import io.mybatis.provider.SqlWrapper;

/**
 * @author qili
 * @date 2025-04-06
 */
@SqlWrapper(LogicalOptimizeSqlWrapper.class)
public interface SysTokenMapper extends BaseMapper<SysToken, Long>, Dao {

}
