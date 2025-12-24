package com.zqqiliyc.framework.web.bean;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
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
public class AuthUserInfoBean implements UserDetails, CredentialsContainer {
    /**
     * 用户id
     */
    private long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
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
    private Set<String> roles;
    /**
     * 用户权限
     */
    private Set<String> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollUtil.isEmpty(permissions)) {
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
        return password;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AuthUserInfoBean.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("nickname='" + nickname + "'")
                .add("avatar='" + avatar + "'")
                .add("role size=" + CollUtil.size(roles))
                .add("permission size=" + CollUtil.size(permissions))
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthUserInfoBean that = (AuthUserInfoBean) o;
        return getId() == that.getId()
                && Objects.equals(getUsername(), that.getUsername())
                && Objects.equals(getNickname(), that.getNickname())
                && Objects.equals(getAvatar(), that.getAvatar())
                && Objects.equals(getRoles(), that.getRoles())
                && Objects.equals(getPermissions(), that.getPermissions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getNickname(), getAvatar(), getRoles(), getPermissions());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", String.valueOf(id));
        map.put("username", username);
        map.put("nickname", nickname);
        map.put("avatar", avatar);
        map.put("roles", CollUtil.join(roles, ","));
        map.put("permissions", CollUtil.join(permissions, ","));
        return map;
    }

    public static AuthUserInfoBean fromMap(Map<Object, Object> map) {
        AuthUserInfoBean bean = new AuthUserInfoBean();
        bean.setId(MapUtil.getLong(map, "id"));
        bean.setUsername(MapUtil.getStr(map, "username"));
        bean.setNickname(MapUtil.getStr(map, "nickname"));
        bean.setAvatar(MapUtil.getStr(map, "avatar"));
        bean.setRoles(CollUtil.newHashSet(MapUtil.getStr(map, "roles").split("[\\s,]+")));
        bean.setPermissions(CollUtil.newHashSet(MapUtil.getStr(map, "permissions").split("[\\s,]+")));
        return bean;
    }

    @Override
    public void eraseCredentials() {
        setPassword("");
    }
}
