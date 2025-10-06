package com.zqqiliyc.biz.core.dto.user;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.biz.core.dto.AbstractQueryDTO;
import com.zqqiliyc.biz.core.entity.SysUser;
import io.mybatis.mapper.example.Example;
import lombok.Getter;
import lombok.Setter;

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
        example.createCriteria().andLike(StrUtil.isNotBlank(key), SysUser::getUsername, "%" + key + "%");
        example.or().andLike(StrUtil.isNotBlank(key), SysUser::getNickname, "%" + key + "%");
    }
}
