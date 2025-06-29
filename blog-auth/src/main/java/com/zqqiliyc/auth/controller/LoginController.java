package com.zqqiliyc.auth.controller;

import com.zqqiliyc.common.constant.WebApiConstants;
import com.zqqiliyc.common.web.http.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qili
 * @date 2025-06-29
 */
@RestController
@RequestMapping(WebApiConstants.API_AUTH_PREFIX + "/login")
public class LoginController {

    public ApiResult<?> login() {

        return ApiResult.success();
    }
}
