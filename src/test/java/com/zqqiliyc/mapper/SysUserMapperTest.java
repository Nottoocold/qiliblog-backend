package com.zqqiliyc.mapper;

import io.mybatis.mapper.example.Example;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author qili
 * @date 2025-04-06
 */
@SpringBootTest
class SysUserMapperTest {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void delete() {
        sysUserMapper.deleteByExample(new Example<>());
        sysUserMapper.deleteHard(new Example<>());
    }
}