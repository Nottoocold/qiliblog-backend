package com.zqqiliyc;

import com.zqqiliyc.domain.dto.role.SysRoleCreateDto;
import com.zqqiliyc.domain.entity.SysRole;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.service.ISysRoleService;
import com.zqqiliyc.service.ISysUserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author qili
 * @date 2025-09-06
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class SystemInit {
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysUserService sysUserService;

    @Test
    public void init() {
        SysRoleCreateDto roleCreateDto = new SysRoleCreateDto();
        roleCreateDto.setCode(SystemConstants.ROLE_ADMIN);
        roleCreateDto.setName("系统管理员");
        roleCreateDto.setSort(0);
        roleCreateDto.setRemark("系统级别角色，不可删除");
        roleCreateDto.setState(0);
    }
}
