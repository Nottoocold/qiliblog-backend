package com.zqqiliyc.mapper;

import com.zqqiliyc.common.IDeleteMapper;
import com.zqqiliyc.domain.entity.SysRole;
import io.mybatis.mapper.logical.LogicalMapper;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface SysRoleMapper extends LogicalMapper<SysRole, Long>, IDeleteMapper<SysRole> {

}
