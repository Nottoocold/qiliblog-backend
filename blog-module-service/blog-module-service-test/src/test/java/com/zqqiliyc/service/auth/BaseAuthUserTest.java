package com.zqqiliyc.service.auth;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.BaseTestApp;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import com.zqqiliyc.framework.web.token.TokenProvider;
import com.zqqiliyc.module.auth.AuthManager;
import com.zqqiliyc.module.svc.system.entity.SysUser;
import com.zqqiliyc.module.svc.system.service.ISysUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 认证 seam 测试的公共夹具：认证要求用户真实存在，故每个用例建一个用户并现签发令牌，
 * 收尾清理认证上下文、令牌、用户缓存与用户，避免污染其他测试
 */
public class BaseAuthUserTest extends BaseTestApp {
    @Autowired
    protected TokenProvider tokenProvider;
    @Autowired
    protected ISysUserService userService;
    @Autowired
    protected AuthManager authManager;

    protected Long userId;
    protected String accessToken;

    @BeforeEach
    public void createAuthUser() {
        SysUser user = new SysUser();
        user.setUsername("auth_seam_" + RandomUtil.randomString(8));
        user.setPassword("auth-seam-password");
        user.setNickname("认证测试用户");
        userId = userService.create(user).getId();
    }

    @AfterEach
    public void cleanAuthUser() {
        SecurityUtils.clearAuthentication();
        if (accessToken != null) {
            tokenProvider.revokeToken(accessToken);
        }
        authManager.clearCache(userId);
        userService.deleteHardByFieldList(SysUser::getId, List.of(userId));
        accessToken = null;
    }

    /**
     * 现签发一个真实有效的访问令牌，收尾统一撤销
     *
     * @return 访问令牌
     */
    protected String issueAccessToken() {
        accessToken = tokenProvider.generateToken(userId, Map.of(SystemConstants.CLAIM_ROLE, "USER")).getAccessToken();
        return accessToken;
    }

    /**
     * 篡改签名，得到校验不通过、库里也查不到的令牌
     *
     * @param token 有效令牌
     * @return 伪造令牌
     */
    protected String forgeToken(String token) {
        String[] parts = token.split("\\.");
        return parts[0] + "." + parts[1] + "." + new StringBuilder(parts[2]).reverse();
    }
}
