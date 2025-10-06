package com.zqqiliyc.admin.controller;

import com.zqqiliyc.biz.core.dto.user.SysUserCreateDTO;
import com.zqqiliyc.biz.core.dto.user.SysUserQueryDTO;
import com.zqqiliyc.biz.core.entity.SysUser;
import com.zqqiliyc.biz.core.service.ISysUserService;
import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.controller.BaseController;
import com.zqqiliyc.framework.web.http.ApiResult;
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
    @PreAuthorize("@ac.hasPermissions('sys:user:query')")
    public ApiResult<PageResult<SysUser>> query(SysUserQueryDTO queryDto) {
        return ApiResult.success(userService.findPageInfo(queryDto));
    }

    @PostMapping
    @PreAuthorize("@ac.hasPermissions({'sys:user:query', 'sys:user:query'}, false)")
    public ApiResult<SysUser> create(@RequestBody SysUserCreateDTO dto) {
        log.info("create user: {}", dto);
        return ApiResult.success();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@ac.hasRoles({'admin', 'user'})")
    public ApiResult<SysUser> update(@PathVariable("id") Long id) {
        log.info("update user id: {}", id);
        System.out.println(getCurrentUser());
        return ApiResult.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ac.hasRoles({'admin', 'user'}, false)")
    public ApiResult<?> delete(@PathVariable("id") Long id) {
        log.info("delete user id: {}", id);
        return ApiResult.success();
    }
}
