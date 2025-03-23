package com.zqqiliyc.mapper;

import com.zqqiliyc.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author qili
 * @date 2025-03-23
 */
@Mapper
public interface SysUserMapper {

    @Select("select * from sys_user where id = #{id}")
    SysUser findById(Long id);
}
