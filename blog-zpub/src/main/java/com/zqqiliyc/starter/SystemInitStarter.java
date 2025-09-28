package com.zqqiliyc.starter;

import com.zqqiliyc.domain.dto.priv.SysPermissionCreateDto;
import com.zqqiliyc.domain.dto.priv.SysRolePrivCreateDto;
import com.zqqiliyc.domain.dto.priv.SysUserRoleCreateDto;
import com.zqqiliyc.domain.dto.role.SysRoleCreateDto;
import com.zqqiliyc.domain.dto.user.SysUserCreateDto;
import com.zqqiliyc.domain.entity.*;
import com.zqqiliyc.framework.common.generater.VirtualPhoneGenerator;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.security.PasswordEncoder;
import com.zqqiliyc.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * 系统初始化启动器：初始化系统默认用户、角色、权限、角色权限关系、用户角色关系
 *
 * @author qili
 * @date 2025-09-06
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class SystemInitStarter implements ApplicationRunner {
    private final ISysPermissionService permissionService;
    private final ISysRoleService sysRoleService;
    private final ISysUserService sysUserService;
    private final ISysRolePrivService rolePrivService;
    private final ISysUserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final PlatformTransactionManager transactionManager;

    @Override
    public void run(ApplicationArguments args) {
        TransactionStatus transaction = transactionManager.getTransaction(TransactionDefinition.withDefaults());
        try {
            initAdminPermission();
            initAdminRole();
            initAdminUser();
            initAuthorization();
            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw new RuntimeException(e);
        }
    }

    private void initAdminPermission() {
        Optional<SysPermission> permission = permissionService.findByCode(SystemConstants.PERMISSION_ADMIN);
        if (permission.isEmpty()) {
            SysPermissionCreateDto dto = new SysPermissionCreateDto();
            dto.setId(1L);
            dto.setCode(SystemConstants.PERMISSION_ADMIN);
            dto.setName("系统管理");
            dto.setParentId(0L);
            dto.setRemark("系统级别权限，不可删除");
            SysPermission sysPermission = permissionService.create(dto);
            Assert.isTrue(sysPermission != null, "系统权限初始化失败");
            Assert.isTrue(sysPermission.getId() == 1L, "系统权限初始化失败");
            Assert.isTrue(SystemConstants.PERMISSION_ADMIN.equals(sysPermission.getCode()), "系统权限初始化失败");
        }
    }

    private void initAdminRole() {
        Optional<SysRole> role = sysRoleService.findByCode(SystemConstants.ROLE_ADMIN);
        if (role.isEmpty()) {
            SysRoleCreateDto dto = new SysRoleCreateDto();
            dto.setId(1L);
            dto.setCode(SystemConstants.ROLE_ADMIN);
            dto.setName("系统管理员");
            dto.setRemark("系统级别角色，不可删除");
            SysRole sysRole = sysRoleService.create(dto);
            Assert.isTrue(sysRole != null, "系统角色初始化失败");
            Assert.isTrue(sysRole.getId() == 1L, "系统角色初始化失败");
            Assert.isTrue(SystemConstants.ROLE_ADMIN.equals(sysRole.getCode()), "系统角色初始化失败");
        }
    }

    private void initAdminUser() {
        Optional<SysUser> user = sysUserService.findByUsername(SystemConstants.USER_ADMIN);
        if (user.isEmpty()) {
            SysUserCreateDto dto = new SysUserCreateDto();
            dto.setId(1L);
            dto.setUsername(SystemConstants.USER_ADMIN);
            dto.setNickname("系统管理员");
            dto.setPassword(passwordEncoder.encode("qiliblog123456."));
            dto.setState(0);
            dto.setEmail("admin@qiliblog.com");
            dto.setPhone(VirtualPhoneGenerator.generate());
            SysUser sysUser = sysUserService.create(dto);
            Assert.isTrue(sysUser != null, "系统用户初始化失败");
            Assert.isTrue(sysUser.getId() == 1L, "系统用户初始化失败");
            Assert.isTrue(SystemConstants.USER_ADMIN.equals(sysUser.getUsername()), "系统用户初始化失败");
        }
    }

    private void initAuthorization() {
        Optional<SysRolePriv> sysRolePriv = rolePrivService.findOne(1L, 1L);
        if (sysRolePriv.isEmpty()) {
            SysRolePrivCreateDto dto = new SysRolePrivCreateDto();
            dto.setRoleId(1L);
            dto.setPrivId(1L);
            SysRolePriv entity = rolePrivService.create(dto);
            Assert.isTrue(entity != null, "系统角色授权初始化失败");
            Assert.isTrue(entity.getRoleId() == 1L, "系统角色授权初始化失败");
            Assert.isTrue(entity.getPrivId() == 1L, "系统角色授权初始化失败");
        }
        Optional<SysUserRole> sysUserRole = userRoleService.findOne(1L, 1L);
        if (sysUserRole.isEmpty()) {
            SysUserRoleCreateDto dto = new SysUserRoleCreateDto();
            dto.setUserId(1L);
            dto.setRoleId(1L);
            SysUserRole entity = userRoleService.create(dto);
            Assert.isTrue(entity != null, "系统用户角色授权初始化失败");
            Assert.isTrue(entity.getUserId() == 1L, "系统用户角色授权初始化失败");
            Assert.isTrue(entity.getRoleId() == 1L, "系统用户角色授权初始化失败");
        }
    }
}
