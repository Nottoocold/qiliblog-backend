package com.zqqiliyc.dto;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.domain.entity.SysUser;
import io.mybatis.mapper.example.Example;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-05-25
 */
@Getter
@Setter
public class UserQueryDto extends AbstractQueryDto<SysUser> {
    private String key;

    @Override
    public Example<SysUser> toExample() {
        Example<SysUser> example = new Example<>();
        Example.Criteria<SysUser> userCriteria = example.createCriteria();
        userCriteria.andLike(StrUtil.isNotBlank(key), SysUser::getNickname, "%" + key + "%");
        example.or().andLike(StrUtil.isNotBlank(key), SysUser::getUsername, "%" + key + "%");
        return example;
    }
}
