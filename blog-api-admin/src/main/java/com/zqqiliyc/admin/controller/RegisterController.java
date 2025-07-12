package com.zqqiliyc.admin.controller;

import com.zqqiliyc.admin.dto.EmailDto;
import com.zqqiliyc.admin.dto.UserRegisterDto;
import com.zqqiliyc.admin.service.IRegisterService;
import com.zqqiliyc.common.constant.WebApiConstants;
import com.zqqiliyc.common.strategy.VerificationCodeService;
import com.zqqiliyc.common.web.http.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hallo
 * @datetime 2025-07-01 11:38
 * @description 注册接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WebApiConstants.API_ADMIN_PREFIX + "/register")
public class RegisterController {

    private final IRegisterService registerService;

    private final VerificationCodeService verificationCodeService;


    /**
     * 发送验证码接口
     * <p>
     * 该接口接收用户邮箱信息，生成验证码并发送到指定邮箱
     * 主要用于用户注册、登录或找回密码等场景
     *
     * @param emailDTO 包含用户邮箱信息的数据传输对象，经过验证确保数据有效性
     * @return 返回一个ApiResult对象，包含操作结果的成功信息
     */
    @PostMapping("/send-code")
    public ApiResult<String> sendVerificationCode(@Valid @RequestBody EmailDto emailDTO) {
        verificationCodeService.generateAndSendCode(emailDTO.getEmail());
        return ApiResult.success("验证码已发送，请查收您的邮箱");
    }

    /**
     * 注册接口
     * <p>
     * 处理用户注册请求
     *
     * @param userRegisterDto 用户注册信息的数据传输对象，包含了用户注册所需的信息
     * @return 返回一个ApiResult对象，包含了注册操作的结果信息
     */
    @PostMapping
    public ApiResult<?> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        return ApiResult.success(registerService.register(userRegisterDto));
    }

}
