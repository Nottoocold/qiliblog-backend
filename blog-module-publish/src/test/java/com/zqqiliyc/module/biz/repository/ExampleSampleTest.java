package com.zqqiliyc.module.biz.repository;

import com.zqqiliyc.module.biz.entity.SysUser;
import com.zqqiliyc.module.biz.repository.mapper.SysUserMapper;
import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.example.ExampleWrapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author qili
 * @date 2025-11-18
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class ExampleSampleTest {
    @Autowired
    SysUserMapper sysUserMapper;

    @Test
    @Order(0)
    public void test_selectByExample() {
        ExampleWrapper<SysUser, Long> exampleWrapper = sysUserMapper.wrapper()
                .or(c -> c.like(SysUser::getUsername, "ad%"),
                        c -> c.like(SysUser::getEmail, "ad%"))
                .eq(SysUser::getId, 1);
        exampleWrapper.list()
                .forEach(System.out::println);
        // 上述查询条件的等价写法：
        Example<SysUser> example = new Example<>();
        Example.Criteria<SysUser> criteria = example.createCriteria();
        Example.OrCriteria<SysUser> orPart = example.orPart();
        orPart.like(SysUser::getUsername, "ad%");
        Example.OrCriteria<SysUser> orPart1 = example.orPart();
        orPart1.like(SysUser::getEmail, "ad%");
        criteria.andOr(List.of(orPart, orPart1));
        criteria.andEqualTo(SysUser::getId, 1);
        sysUserMapper.selectByExample(example)
                .forEach(System.out::println);
    }
}
