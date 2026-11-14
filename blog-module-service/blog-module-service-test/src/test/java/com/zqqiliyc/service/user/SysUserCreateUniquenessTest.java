package com.zqqiliyc.service.user;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.BaseTestApp;
import com.zqqiliyc.framework.common.generater.VirtualPhoneGenerator;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.module.svc.system.entity.SysUser;
import com.zqqiliyc.module.svc.system.service.ISysUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户唯一性规则的行为测试：邮箱/用户名/手机号重复时创建必须抛业务异常，
 * 规则归属 SysUserService.beforeCreate，注册与管理端建用户共用同一条规则
 */
public class SysUserCreateUniquenessTest extends BaseTestApp {
    @Autowired
    private ISysUserService userService;

    /**
     * 本用例尝试创建过的用户名，收尾按用户名物理删除，重复校验失效时也能清干净
     */
    private final List<String> attemptedUsernames = new ArrayList<>();

    private String existingUsername;
    private String existingEmail;
    private String existingPhone;

    @BeforeEach
    public void createExistingUser() {
        String suffix = RandomUtil.randomString(8);
        existingUsername = "uniq_" + suffix;
        existingEmail = "uniq_" + suffix + "@example.com";
        existingPhone = VirtualPhoneGenerator.generate();

        SysUser existing = newUser(existingUsername);
        existing.setEmail(existingEmail);
        existing.setPhone(existingPhone);
        userService.create(existing);
    }

    @AfterEach
    public void cleanUsers() {
        List<Long> userIds = attemptedUsernames.stream()
                .map(userService::findByUsername)
                .filter(Objects::nonNull)
                .map(SysUser::getId)
                .toList();
        if (!userIds.isEmpty()) {
            userService.deleteHardByFieldList(SysUser::getId, userIds);
        }
        attemptedUsernames.clear();
    }

    @Test
    @Order(0)
    public void testCreateWithExistingEmailThrowsClientException() {
        SysUser candidate = newUser("uniq_other_" + RandomUtil.randomString(8));
        candidate.setEmail(existingEmail);

        assertCreateRejected(candidate, GlobalErrorDict.EMAIL_EXISTS);
    }

    @Test
    @Order(1)
    public void testCreateWithExistingUsernameThrowsClientException() {
        SysUser candidate = newUser(existingUsername);
        candidate.setEmail("uniq_other_" + RandomUtil.randomString(8) + "@example.com");

        assertCreateRejected(candidate, GlobalErrorDict.USERNAME_EXISTS);
    }

    @Test
    @Order(2)
    public void testCreateWithExistingPhoneThrowsClientException() {
        SysUser candidate = newUser("uniq_other_" + RandomUtil.randomString(8));
        candidate.setPhone(existingPhone);

        assertCreateRejected(candidate, GlobalErrorDict.PHONE_EXISTS);
    }

    @Test
    @Order(3)
    public void testCreateWithoutEmailAndPhoneSucceeds() {
        SysUser candidate = newUser("uniq_blank_" + RandomUtil.randomString(8));

        Assertions.assertNotNull(userService.create(candidate).getId(), "邮箱与手机号为空时不参与唯一性校验");
    }

    private void assertCreateRejected(SysUser candidate, GlobalErrorDict expectedError) {
        ClientException exception = Assertions.assertThrowsExactly(ClientException.class,
                () -> userService.create(candidate));

        Assertions.assertEquals(expectedError.getCode(), exception.getStatus());
        Assertions.assertEquals(expectedError.getMessage(), exception.getMessage());
    }

    private SysUser newUser(String username) {
        attemptedUsernames.add(username);
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword("uniqueness-password");
        user.setNickname("唯一性测试用户");
        return user;
    }
}
