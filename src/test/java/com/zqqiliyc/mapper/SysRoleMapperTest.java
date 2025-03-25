package com.zqqiliyc.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zqqiliyc.domain.entity.SysRole;
import org.apache.ibatis.executor.BatchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qili
 * @date 2025-03-26
 */
@SpringBootTest
class SysRoleMapperTest {
    @Autowired
    SysRoleMapper roleMapper;

    @Test
    public void testInsert() {
        int size = 10;
        List<SysRole> roles = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysRole role = new SysRole();
            role.setCode("测试角色" + i);
            role.setName("测试角色" + i);
            role.setSort(i);
            role.setState(1);
            role.setRemark("测试角色" + i);
            if (null == findByCode(role.getCode())) {
                roles.add(role);
            }
        }
        List<BatchResult> rows = roleMapper.insert(roles);
        rows.forEach(System.out::println);
    }


    private SysRole findByCode(String code) {
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery(SysRole.class)
                .eq(SysRole::getCode, code);
        return roleMapper.selectOne(queryWrapper);
    }
}