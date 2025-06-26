package com.zqqiliyc.admin.controller;

import com.github.pagehelper.PageInfo;
import com.zqqiliyc.admin.dto.UserCreateDto;
import com.zqqiliyc.admin.dto.UserQueryDto;
import com.zqqiliyc.common.constant.WebApiConstants;
import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PageInfo<SysUser>> query(UserQueryDto queryDto) {
        return ResponseEntity.ok(userService.findPageInfo(queryDto));
    }

    @PostMapping
    public ResponseEntity<SysUser> create(@RequestBody UserCreateDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }
}
