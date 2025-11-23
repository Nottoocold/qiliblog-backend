package com.zqqiliyc.module.auth.controller;

import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.controller.BaseController;
import com.zqqiliyc.framework.web.http.ApiResult;
import com.zqqiliyc.module.auth.bean.AuthResult;
import com.zqqiliyc.module.auth.dto.LoginDTO;
import com.zqqiliyc.module.auth.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    public ApiResult<AuthResult> login(@Valid @RequestBody LoginDTO loginDto) {
        return ApiResult.success(authService.login(loginDto));
    }

    @PostMapping("/logout")
    public ApiResult<Void> logout(@RequestHeader(value = SystemConstants.HEADER_AUTHORIZATION, required = false) String ak) {
        authService.logout(ak);
        return ApiResult.success();
    }

    @GetMapping("/userinfo")
    public ApiResult<AuthUserInfoBean> userinfo() {
        AuthUserInfoBean currentUser = getCurrentUser();
        if (Objects.nonNull(currentUser)) {
            return ApiResult.success(currentUser);
        }
        return ApiResult.success(authService.userinfo(getAuthentication().getCredentials().toString()));
    }

    @PostMapping("/refresh")
    public ApiResult<AuthResult> refresh(@RequestParam("refresh_token") String refreshToken) {
        return ApiResult.success(authService.refreshToken(refreshToken));
    }
}
