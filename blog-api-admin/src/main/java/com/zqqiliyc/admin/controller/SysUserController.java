package com.zqqiliyc.admin.controller;

import com.github.pagehelper.PageInfo;
import com.zqqiliyc.admin.dto.UserCreateDto;
import com.zqqiliyc.admin.dto.UserQueryDto;
import com.zqqiliyc.common.constant.WebApiConstants;
import com.zqqiliyc.common.web.http.ApiResult;
import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

/**
 * @author qili
 * @date 2025-05-21
 */
@Slf4j
@RestController
@RequestMapping(WebApiConstants.API_ADMIN_PREFIX + "/user")
@RequiredArgsConstructor
public class SysUserController {
    private final ISysUserService userService;

    @GetMapping
    @RequiresPermissions("user:read")
    public ApiResult<PageInfo<SysUser>> query(UserQueryDto queryDto) {
        return ApiResult.success(userService.findPageInfo(queryDto));
    }

    @PostMapping
    @RequiresPermissions("sys:user:create")
    public ApiResult<SysUser> create(@RequestBody UserCreateDto dto) {
        return ApiResult.success(userService.create(dto));
    }

    @PutMapping("/{id}")
    public ApiResult<SysUser> update(@PathVariable("id") Long id) {
        log.info("update user id: {}", id);
        return ApiResult.success();
    }

    @DeleteMapping("/{id}")
    public ApiResult<?> delete(@PathVariable("id") Long id) {
        log.info("delete user id: {}", id);
        return ApiResult.success();
    }
}
