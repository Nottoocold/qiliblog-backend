package com.zqqiliyc.auth.controller;

import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.service.ILoginService;
import com.zqqiliyc.common.constant.WebApiConstants;
import com.zqqiliyc.common.web.http.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qili
 * @date 2025-06-29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WebApiConstants.API_AUTH_PREFIX + "/login")
public class LoginController {
    private final ILoginService loginService;

    @PostMapping
    public ApiResult<AuthResult> login(@Valid @RequestBody LoginDto loginDto) {
        return ApiResult.success(loginService.login(loginDto));
    }
}
