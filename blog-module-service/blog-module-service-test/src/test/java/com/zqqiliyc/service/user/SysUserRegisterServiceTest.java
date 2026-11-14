package com.zqqiliyc.service.user;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.BaseTestApp;
import com.zqqiliyc.framework.common.generater.VirtualPhoneGenerator;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.module.svc.system.config.prop.VerificationProperties;
import com.zqqiliyc.module.svc.system.dto.user.SysUserRegisterDto;
import com.zqqiliyc.module.svc.system.entity.SysUser;
import com.zqqiliyc.module.svc.system.service.ISysUserRegisterService;
import com.zqqiliyc.module.svc.system.service.ISysUserService;
import com.zqqiliyc.module.svc.system.service.VerificationCodeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * 邮箱注册链路的行为测试：发验证码 → 注册 → 用户落库，
 * 覆盖密码编码、用户名/手机号生成规则、注册方式与邮箱校验、验证码错误与防重放。
 * test profile 下 verification.enabled=false，验证码恒为全 '0'
 */
public class SysUserRegisterServiceTest extends BaseTestApp {
    private static final int EMAIL_REGISTER_TYPE = 0;
    private static final String NICKNAME = "注册测试用户";

    @Autowired
    private ISysUserRegisterService registerService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private VerificationProperties verificationProperties;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String email;
    private String password;

    @BeforeEach
    public void prepareRegisterRequest() {
        email = "register_" + RandomUtil.randomString(8) + "@example.com";
        password = "register-password-1";
    }

    @AfterEach
    public void cleanRegisteredUser() {
        SysUser registered = userService.findByEmail(email);
        if (registered != null) {
            userService.deleteHardByFieldList(SysUser::getId, List.of(registered.getId()));
        }
    }

    @Test
    @Order(0)
    public void testRegisterCreatesUserWithEncodedPasswordAndGeneratedFields() {
        verificationCodeService.generateAndSendCode(email);

        registerService.register(newRegisterDto());

        SysUser registered = userService.findByEmail(email);
        Assertions.assertNotNull(registered, "注册成功后用户应落库");
        Assertions.assertNotEquals(password, registered.getPassword(), "密码不得明文落库");
        Assertions.assertTrue(passwordEncoder.matches(password, registered.getPassword()), "密码应是编码后的口令");
        Assertions.assertTrue(registered.getUsername().matches("user_\\d{1,8}"), "用户名应是 user_ 加雪花 ID 后 8 位");
        Assertions.assertTrue(VirtualPhoneGenerator.isVirtualPhone(registered.getPhone()), "手机号应是生成的虚拟运营商号段");
        Assertions.assertEquals(NICKNAME, registered.getNickname());
        Assertions.assertEquals(0, registered.getState().intValue());
        Assertions.assertEquals(-1L, registered.getDeptId().longValue());
    }

    @Test
    @Order(1)
    public void testRegisterWithExistingEmailThrowsClientException() {
        SysUser existing = new SysUser();
        existing.setUsername("register_existing_" + RandomUtil.randomString(8));
        existing.setPassword("existing-password");
        existing.setNickname(NICKNAME);
        existing.setEmail(email);
        Long existingUserId = userService.create(existing).getId();
        verificationCodeService.generateAndSendCode(email);

        assertRegisterRejected(newRegisterDto(), GlobalErrorDict.EMAIL_EXISTS);

        Assertions.assertEquals(existingUserId, userService.findByEmail(email).getId(), "重复邮箱不应再建用户");
    }

    @Test
    @Order(2)
    public void testRegisterWithWrongCodeThrowsClientException() {
        verificationCodeService.generateAndSendCode(email);
        SysUserRegisterDto registerDto = newRegisterDto();
        registerDto.setCode(StrUtil.repeat('1', verificationProperties.getCodeLength()));

        assertRegisterRejected(registerDto, GlobalErrorDict.INVALID_CODE);

        Assertions.assertNull(userService.findByEmail(email), "验证码错误不应建用户");
    }

    @Test
    @Order(3)
    public void testRegisterWithoutSendingCodeThrowsClientException() {
        assertRegisterRejected(newRegisterDto(), GlobalErrorDict.INVALID_CODE);

        Assertions.assertNull(userService.findByEmail(email), "未发送验证码不应建用户");
    }

    @Test
    @Order(4)
    public void testRegisterRejectsReplayedCode() {
        verificationCodeService.generateAndSendCode(email);
        registerService.register(newRegisterDto());
        Long registeredUserId = userService.findByEmail(email).getId();
        userService.deleteHardByFieldList(SysUser::getId, List.of(registeredUserId));

        // 邮箱已腾空，但验证码在首次注册时已被消耗，同一验证码不能再注册
        assertRegisterRejected(newRegisterDto(), GlobalErrorDict.INVALID_CODE);

        Assertions.assertNull(userService.findByEmail(email), "重放验证码不应建用户");
    }

    @Test
    @Order(5)
    public void testRegisterWithUnsupportedRegisterTypeThrowsClientException() {
        SysUserRegisterDto registerDto = newRegisterDto();
        registerDto.setRegisterType(1);

        assertRegisterRejected(registerDto, GlobalErrorDict.UNSUPPORTED_REGISTER_TYPE);

        Assertions.assertNull(userService.findByEmail(email), "不支持的注册方式不应建用户");
    }

    @Test
    @Order(6)
    public void testRegisterWithBlankEmailThrowsClientException() {
        SysUserRegisterDto registerDto = newRegisterDto();
        registerDto.setEmail(" ");

        assertRegisterRejected(registerDto, GlobalErrorDict.PARAM_ERROR, "邮箱不能为空");
    }

    @Test
    @Order(7)
    public void testRegisterWithMalformedEmailThrowsClientException() {
        SysUserRegisterDto registerDto = newRegisterDto();
        registerDto.setEmail("not-an-email");

        assertRegisterRejected(registerDto, GlobalErrorDict.PARAM_ERROR, "邮箱格式不正确");
    }

    private void assertRegisterRejected(SysUserRegisterDto registerDto, GlobalErrorDict expectedError) {
        assertRegisterRejected(registerDto, expectedError, expectedError.getMessage());
    }

    private void assertRegisterRejected(SysUserRegisterDto registerDto, GlobalErrorDict expectedError, String expectedMessage) {
        ClientException exception = Assertions.assertThrowsExactly(ClientException.class,
                () -> registerService.register(registerDto));

        Assertions.assertEquals(expectedError.getCode(), exception.getStatus());
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    /**
     * @return 邮箱注册请求：用户名与手机号留空，由注册服务生成
     */
    private SysUserRegisterDto newRegisterDto() {
        SysUserRegisterDto registerDto = new SysUserRegisterDto();
        registerDto.setEmail(email);
        registerDto.setPassword(password);
        registerDto.setNickname(NICKNAME);
        registerDto.setCode(StrUtil.repeat('0', verificationProperties.getCodeLength()));
        registerDto.setRegisterType(EMAIL_REGISTER_TYPE);
        return registerDto;
    }
}
