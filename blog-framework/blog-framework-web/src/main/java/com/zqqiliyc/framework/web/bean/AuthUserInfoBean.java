package com.zqqiliyc.framework.web.bean;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * @author qili
 * @date 2025-07-16
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"authorities", "password",
        "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
public class AuthUserInfoBean implements UserDetails {
    /**
     * 用户id
     */
    private long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户角色
     */
    private String[] roles;
    /**
     * 用户权限
     */
    private String[] permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions == null || permissions.length == 0) {
            return Collections.emptySet();
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AuthUserInfoBean.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("nickname='" + nickname + "'")
                .add("avatar='" + avatar + "'")
                .add("role size=" + ArrayUtil.length(roles))
                .add("permission size=" + ArrayUtil.length(permissions))
                .toString();
    }
}
