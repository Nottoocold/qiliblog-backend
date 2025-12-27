package com.zqqiliyc.module.biz.service;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.module.biz.entity.SysUser;
import com.zqqiliyc.module.svc.system.dto.user.SysUserCreateDTO;
import com.zqqiliyc.module.svc.system.dto.user.SysUserQueryDTO;
import com.zqqiliyc.module.svc.system.service.ISysUserService;
import io.mybatis.mapper.example.Example;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qili
 * @date 2025-04-06
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class SysUserServiceTest {
    @Autowired
    private ISysUserService sysUserService;
    final int count = 100;

    @BeforeEach
    public void insert() {
        for (int i = 0; i < count; i++) {
            SysUserCreateDTO sysUser = new SysUserCreateDTO();
            sysUser.setUsername(RandomUtil.randomString(6));
            sysUser.setNickname(RandomUtil.randomString(6));
            sysUser.setPassword(RandomUtil.randomString(18));
            sysUser.setEmail(RandomUtil.randomString(6) + "@qq.com");
            sysUser.setPhone(RandomUtil.randomNumbers(11));
            sysUser.setAvatar("https://avatars.githubusercontent.com/u/102040668?v=4");
            sysUserService.create(sysUser);
        }
    }

    @AfterEach
    public void clear() {

    }

    @Test
    @Transactional
    public void testFindList() {
        SysUserQueryDTO queryDto = new SysUserQueryDTO();
        queryDto.setKey(RandomUtil.randomString(3));
        Example<SysUser> example = queryDto.toExample();
        List<SysUser> sysUsers = sysUserService.findList(example);
        System.out.println(sysUsers);
    }
}