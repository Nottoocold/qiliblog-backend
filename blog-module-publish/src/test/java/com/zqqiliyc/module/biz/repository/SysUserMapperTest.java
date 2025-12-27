package com.zqqiliyc.module.biz.repository;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.module.biz.entity.SysUser;
import com.zqqiliyc.module.svc.system.mapper.SysUserMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author qili
 * @date 2025-04-06
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class SysUserMapperTest {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    @Transactional
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
            assertEquals(1, inserted);
        }
    }
}