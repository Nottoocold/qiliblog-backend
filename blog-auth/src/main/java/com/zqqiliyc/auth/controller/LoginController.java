package com.zqqiliyc.auth.controller;

import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.service.ILoginService;
import com.zqqiliyc.framework.web.AuthUserInfoBean;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.controller.BaseController;
import com.zqqiliyc.framework.web.token.TokenProvider;
import com.zqqiliyc.framework.web.utils.SecurityUtils;
import com.zqqiliyc.framework.web.http.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qili
 * @date 2025-06-29
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(WebApiConstants.API_AUTH_PREFIX)
public class LoginController extends BaseController {
    private final ILoginService loginService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ApiResult<AuthResult> login(@Valid @RequestBody LoginDto loginDto) {
        return ApiResult.success(loginService.login(loginDto));
    }

    @PostMapping("/logout")
    public ApiResult<Void> logout() {
        String accessToken = getAuthentication().getCredentials().toString();
        AuthUserInfoBean currentUser = getCurrentUser();
        log.info("用户 {} 退出登录", currentUser.getUsername());
        SecurityUtils.clearAuthentication();
        tokenProvider.revokeToken(accessToken);
        return ApiResult.success();
    }
}
