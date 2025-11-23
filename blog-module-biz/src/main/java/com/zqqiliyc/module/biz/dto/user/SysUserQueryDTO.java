package com.zqqiliyc.module.biz.dto.user;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.module.biz.dto.AbstractQueryDTO;
import com.zqqiliyc.module.biz.entity.SysUser;
import io.mybatis.mapper.example.Example;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author qili
 * @date 2025-05-25
 */
@Getter
@Setter
public class SysUserQueryDTO extends AbstractQueryDTO<SysUser> {
    private String key;

    @Override
    protected void fillExample(Example<SysUser> example) {
        Example.Criteria<SysUser> criteria = example.createCriteria();
        if (StrUtil.isNotBlank(key)) {
            criteria.andOr(List.of(
                    example.orPart().like(SysUser::getUsername, "%" + key + "%"),
                    example.orPart().like(SysUser::getNickname, "%" + key + "%")
            ));
        }
    }
}
