package com.zqqiliyc.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zqqiliyc.domain.entity.SysRolePriv;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author qili
 * @date 2025-03-26
 */
@SpringBootTest
class SysRolePermissionMapperTest {
    @Autowired
    SysRolePermissionMapper rolePermissionMapper;

    @Test
    void test() {
        int size = 10;
        long roleId = 1;
        int c = 0;
        for (int i = 0; i < size; i++) {
            SysRolePriv rolePriv = new SysRolePriv();
            rolePriv.setRoleId(roleId);
            rolePriv.setPrivId((long) (i + 1));
            if (null == findByIden(roleId, i + 1))
                c = c + rolePermissionMapper.insert(rolePriv);
        }
    }

    private SysRolePriv findByIden(long roleId, long privId) {
        LambdaQueryWrapper<SysRolePriv> queryWrapper = Wrappers.lambdaQuery(SysRolePriv.class)
                .eq(SysRolePriv::getRoleId, roleId).eq(SysRolePriv::getPrivId, privId);
        return rolePermissionMapper.selectOne(queryWrapper);
    }
}