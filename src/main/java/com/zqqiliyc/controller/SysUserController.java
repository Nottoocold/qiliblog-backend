package com.zqqiliyc.controller;

import com.zqqiliyc.domain.entity.SysUser;
import com.zqqiliyc.dto.UserCreateDto;
import com.zqqiliyc.service.ISysUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qili
 * @date 2025-05-21
 */
@RestController
@RequestMapping(path = "/api/user")
public class SysUserController {
    private final ISysUserService userService;

    public SysUserController(ISysUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<SysUser> create(@RequestBody UserCreateDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }
}
