package com.zqqiliyc.service;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.domain.entity.BaseEntity;
import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.dto.UserQueryDto;
import io.mybatis.mapper.example.Example;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qili
 * @date 2025-04-06
 */
@SpringBootTest
class SysUserServiceTest {
    @Autowired
    private ISysUserService iSysUserService;
    final int count = 100;
    private static final List<SysUser> toDeleted = new ArrayList<>();

    @AfterEach
    public void clear() {
        List<Long> ids = toDeleted.stream().map(SysUser::getId).toList();
        int updated = iSysUserService.deleteByFieldList(BaseEntity::getId, ids);
        int deleted = iSysUserService.deleteHardByFieldList(BaseEntity::getId, ids);
        Assertions.assertEquals(count, updated);
        Assertions.assertEquals(count, deleted);
    }

    @BeforeEach
    public void insert() {
        for (int i = 0; i < count; i++) {
            SysUser sysUser = new SysUser();
            sysUser.setUsername(RandomUtil.randomString(6));
            sysUser.setNickname(RandomUtil.randomString(6));
            sysUser.setPassword(RandomUtil.randomString(18));
            sysUser.setEmail(RandomUtil.randomString(6) + "@qq.com");
            sysUser.setPhone(RandomUtil.randomNumbers(11));
            sysUser.setAvatar("https://avatars.githubusercontent.com/u/102040668?v=4");
            toDeleted.add(iSysUserService.saveOrUpdate(sysUser));
        }
    }

    @Test
    public void testFindList() {
        UserQueryDto queryDto = new UserQueryDto();
        queryDto.setKey(RandomUtil.randomString(3));
        Example<SysUser> example = queryDto.toExample();
        List<SysUser> sysUsers = iSysUserService.findList(example);
        System.out.println(sysUsers);
    }
}