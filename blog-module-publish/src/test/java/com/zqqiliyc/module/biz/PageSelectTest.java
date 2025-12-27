package com.zqqiliyc.module.biz;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.module.biz.entity.SysUser;
import com.zqqiliyc.module.svc.system.dto.user.SysUserCreateDTO;
import com.zqqiliyc.module.svc.system.dto.user.SysUserQueryDTO;
import com.zqqiliyc.module.svc.system.service.ISysUserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-06-07
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class PageSelectTest {
    @Autowired
    private ISysUserService userService;
    static final int count = 100;

    @BeforeEach
    public void setUp() {
        for (int i = 0; i < count; i++) {
            SysUserCreateDTO sysUser = new SysUserCreateDTO();
            sysUser.setUsername(RandomUtil.randomString(6));
            sysUser.setNickname(RandomUtil.randomString(6));
            sysUser.setPassword(RandomUtil.randomString(18));
            sysUser.setEmail(RandomUtil.randomString(6) + "@qq.com");
            sysUser.setPhone(RandomUtil.randomNumbers(11));
            sysUser.setAvatar("https://avatars.githubusercontent.com/u/102040668?v=4");
            userService.create(sysUser);
        }
    }

    @AfterEach
    public void setDown() {

    }

    @Order(0)
    @Test
    @Transactional
    public void testNoPage() {
        SysUserQueryDTO queryDto = new SysUserQueryDTO();
        queryDto.setSortBy("username asc,state,id desc");

        Assertions.assertThrowsExactly(ClientException.class, () -> userService.findPageInfo(queryDto));
    }

    @Order(1)
    @Test
    @Transactional
    public void testPage() {
        int pageNum = 1;
        int pageSize = 10;
        SysUserQueryDTO queryDto = new SysUserQueryDTO();
        queryDto.setSortBy("username asc,state,id desc");
        queryDto.setCurrent(pageNum);
        queryDto.setPageSize(pageSize);

        PageResult<SysUser> pageInfo = userService.findPageInfo(queryDto);

        Assertions.assertEquals(pageNum, pageInfo.getCurrent());
        Assertions.assertEquals(pageSize, pageInfo.getPageSize());
        Assertions.assertFalse(pageInfo.isHasPre());
        Assertions.assertTrue(pageInfo.isHasNext());
    }
}
