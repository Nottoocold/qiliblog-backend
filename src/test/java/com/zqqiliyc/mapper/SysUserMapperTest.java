package com.zqqiliyc.mapper;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.domain.entity.SysUser;
import io.mybatis.mapper.example.Example;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

    @AfterEach
    public void clear() {
        sysUserMapper.deleteHard(new Example<>());
    }

    @Test
    public void insert() {
        final int count = 100;
        for (int i = 0; i < count; i++) {
            SysUser sysUser = new SysUser();
            sysUser.setUsername(RandomUtil.randomString(6));
            sysUser.setNickname(RandomUtil.randomString(6));
            sysUser.setPassword(RandomUtil.randomString(18));
            sysUser.setEmail(RandomUtil.randomString(6) + "@qq.com");
            sysUser.setPhone(RandomUtil.randomNumbers(11));
            sysUser.setAvatar("https://avatars.githubusercontent.com/u/102040668?v=4");
            int inserted = sysUserMapper.insertSelective(sysUser);
            Assertions.assertEquals(1, inserted);
        }
    }
}