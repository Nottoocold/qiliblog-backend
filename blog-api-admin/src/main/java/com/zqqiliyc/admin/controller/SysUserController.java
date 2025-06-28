package com.zqqiliyc.admin.controller;

import com.github.pagehelper.PageInfo;
import com.zqqiliyc.admin.dto.UserCreateDto;
import com.zqqiliyc.admin.dto.UserQueryDto;
import com.zqqiliyc.common.constant.WebApiConstants;
import com.zqqiliyc.common.web.http.ApiResult;
import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author qili
 * @date 2025-05-21
 */
@RestController
@RequestMapping(WebApiConstants.API_ADMIN_PREFIX + "/user")
@RequiredArgsConstructor
public class SysUserController {
    private final ISysUserService userService;

    @GetMapping
    public ApiResult<PageInfo<SysUser>> query(UserQueryDto queryDto) {
        return ApiResult.success(userService.findPageInfo(queryDto));
    }

    @PostMapping
    public ApiResult<SysUser> create(@RequestBody UserCreateDto dto) {
        return ApiResult.success(userService.create(dto));
    }
}
