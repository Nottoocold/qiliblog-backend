package com.zqqiliyc.admin.controller;

import com.zqqiliyc.admin.dto.UserCreateDto;
import com.zqqiliyc.admin.dto.UserQueryDto;
import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.controller.BaseController;
import com.zqqiliyc.framework.web.http.ApiResult;
import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@ac.cp('sys:user:query')")
    public ApiResult<PageResult<SysUser>> query(UserQueryDto queryDto) {
        return ApiResult.success(userService.findPageInfo(queryDto));
    }

    @PostMapping
    @PreAuthorize("@ac.cp(false , {'sys:user:query', 'sys:user:query'})")
    public ApiResult<SysUser> create(@RequestBody UserCreateDto dto) {
        log.info("create user: {}", dto);
        return ApiResult.success();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@ac.cr(true , {'sys:user:query', 'sys:user:update'})")
    public ApiResult<SysUser> update(@PathVariable("id") Long id) {
        log.info("update user id: {}", id);
        System.out.println(getCurrentUser());
        return ApiResult.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ac.cr(false , {'sys:user:delete', 'sys:user:delete'})")
    public ApiResult<?> delete(@PathVariable("id") Long id) {
        log.info("delete user id: {}", id);
        return ApiResult.success();
    }
}
