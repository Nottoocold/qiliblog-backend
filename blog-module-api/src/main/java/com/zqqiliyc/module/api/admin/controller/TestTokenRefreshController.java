package com.zqqiliyc.module.api.admin.controller;

import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.http.ApiResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qili
 * @date 2025-09-21
 */
@RestController
@RequestMapping(WebApiConstants.API_ADMIN_PREFIX + "/protect")
public class TestTokenRefreshController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<String> get() {
        return ApiResult.success("get protect data");
    }
}
