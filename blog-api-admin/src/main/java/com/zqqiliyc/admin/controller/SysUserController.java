package com.zqqiliyc.admin.controller;

import com.zqqiliyc.admin.dto.UserCreateDto;
import com.zqqiliyc.admin.dto.UserQueryDto;
import com.zqqiliyc.common.bean.PageResult;
import com.zqqiliyc.common.constant.WebApiConstants;
import com.zqqiliyc.common.controller.BaseController;
import com.zqqiliyc.common.web.http.ApiResult;
import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author qili
 * @date 2025-05-21
 */
@Slf4j
@RestController
@RequestMapping(WebApiConstants.API_ADMIN_PREFIX + "/user")
@RequiredArgsConstructor
public class SysUserController extends BaseController {
    private final ISysUserService userService;

    @GetMapping
    public ApiResult<PageResult<SysUser>> query(UserQueryDto queryDto) {
        return ApiResult.success(userService.findPageInfo(queryDto));
    }

    @PostMapping
    public ApiResult<SysUser> create(@RequestBody UserCreateDto dto) {
        return ApiResult.success(userService.create(dto));
    }

    @PutMapping("/{id}")
    public ApiResult<SysUser> update(@PathVariable("id") Long id) {
        log.info("update user id: {}", id);
        System.out.println(getCurrentUser());
        return ApiResult.success();
    }

    @DeleteMapping("/{id}")
    public ApiResult<?> delete(@PathVariable("id") Long id) {
        log.info("delete user id: {}", id);
        return ApiResult.success();
    }
}
