package com.zqqiliyc;

import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zqqiliyc.admin.dto.UserCreateDto;
import com.zqqiliyc.admin.dto.UserQueryDto;
import com.zqqiliyc.domain.entity.BaseEntity;
import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.service.ISysUserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

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
    private final List<SysUser> toDeleted = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        toDeleted.clear();
        for (int i = 0; i < count; i++) {
            UserCreateDto sysUser = new UserCreateDto();
            sysUser.setUsername(RandomUtil.randomString(6));
            sysUser.setNickname(RandomUtil.randomString(6));
            sysUser.setPassword(RandomUtil.randomString(18));
            sysUser.setEmail(RandomUtil.randomString(6) + "@qq.com");
            sysUser.setPhone(RandomUtil.randomNumbers(11));
            sysUser.setAvatar("https://avatars.githubusercontent.com/u/102040668?v=4");
            toDeleted.add(userService.create(sysUser));
        }
    }

    @AfterEach
    public void setDown() {
        List<Long> ids = toDeleted.stream().map(SysUser::getId).toList();
        userService.deleteHardByFieldList(BaseEntity::getId, ids);
        toDeleted.clear();
    }

    @Order(0)
    @Test
    public void testNoPage() {
        UserQueryDto queryDto = new UserQueryDto();
        queryDto.setOrderBy("username desc");
        Page<SysUser> result = userService.findPage(queryDto);
        PageInfo<SysUser> pageInfo = userService.findPageInfo(queryDto);
        Assertions.assertFalse(result.getTotal() > 0);
        Assertions.assertFalse(pageInfo.hasContent());
    }

    @Order(1)
    @Test
    public void testPage() {
        int pageNum = 1;
        int pageSize = 10;
        UserQueryDto queryDto = new UserQueryDto();
        queryDto.setOrderBy("username desc");
        queryDto.setPageNum(pageNum);
        queryDto.setPageSize(pageSize);
        Page<SysUser> result = userService.findPage(queryDto);
        PageInfo<SysUser> pageInfo = userService.findPageInfo(queryDto);

        Assertions.assertEquals(10, result.getResult().size());
        Assertions.assertEquals(pageNum, result.getPageNum());
        Assertions.assertEquals(pageSize, result.getPageSize());

        Assertions.assertEquals(10, pageInfo.getSize());
        Assertions.assertEquals(pageNum, pageInfo.getPageNum());
        Assertions.assertEquals(pageSize, pageInfo.getPageSize());
        Assertions.assertFalse(pageInfo.isHasPreviousPage());
        Assertions.assertTrue(pageInfo.isHasNextPage());
    }
}
