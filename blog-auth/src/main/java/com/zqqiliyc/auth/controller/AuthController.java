package com.zqqiliyc.auth.controller;

import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.service.IAuthService;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.controller.BaseController;
import com.zqqiliyc.framework.web.http.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author qili
 * @date 2025-06-29
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebApiConstants.API_AUTH_PREFIX)
public class AuthController extends BaseController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ApiResult<AuthResult> login(@Valid @RequestBody LoginDto loginDto) {
        return ApiResult.success(authService.login(loginDto));
    }

    @PostMapping("/logout")
    public ApiResult<Void> logout(@RequestHeader(SystemConstants.HEADER_AUTHORIZATION) String accessToken) {
        AuthUserInfoBean currentUser = getCurrentUser();
        log.info("用户 {} 退出登录", currentUser.getUsername());
        authService.logout(accessToken);
        return ApiResult.success();
    }

    @PostMapping("/refresh")
    public ApiResult<AuthResult> refresh(@RequestParam String refreshToken) {
        return ApiResult.success(authService.refreshToken(refreshToken));
    }
}
