package com.zqqiliyc.admin.controller;

import com.zqqiliyc.common.constant.WebApiConstants;
import com.zqqiliyc.common.web.http.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author qili
 * @date 2025-06-27
 */
@Slf4j
@RestController
@RequestMapping(WebApiConstants.API_AUTH_PREFIX)
public class SysRegisterController {

    @PostMapping("/register")
    public ApiResult<?> register(@RequestBody Map<String, String> params) {
        log.info("register body: {}", params);
        return ApiResult.success("register success");
    }
}
