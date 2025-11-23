package com.zqqiliyc.module.biz.dto.user;

import com.zqqiliyc.module.biz.dto.CreateDTO;
import com.zqqiliyc.module.biz.entity.SysUser;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户创建数据传输对象（DTO）
 *
 * <p>用于接收业务层传递的用户创建请求参数</p>
 *
 * <h3>字段说明：</h3>
 * <ul>
 *     <li>{@link #username}：登录用户名，必须唯一</li>
 *     <li>{@link #password}：密码，建议已加密形式传入（如 SHA256 加密） </li>
 *     <li>{@link #nickname}：用户昵称，可选</li>
 *     <li>{@link #state}：用户状态，0:正常，1:禁用</li>
 *     <li>{@link #email}：邮箱地址，可用于找回密码</li>
 *     <li>{@link #phone}：手机号码，可选</li>
 *     <li>{@link #avatar}：头像图片 URL 地址，可选</li>
 *     <li>{@link #deptId}：所属部门 ID，系统内部标识</li>
 * </ul>
 *
 * @author hallo
 * @datetime 2025-07-01 19:00
 */
@Getter
@Setter
public class SysUserCreateDTO implements CreateDTO<SysUser> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名 登录名
     */
    private String username;
    /**
     * 密码 sha256加密
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 状态 0:正常 1:禁用
     */
    private Integer state;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像url
     */
    private String avatar;
    /**
     * 部门id
     */
    private Long deptId;

    @Override
    public SysUser toEntity() {
        SysUser user = new SysUser();
        user.setId(getId());
        user.setUsername(getUsername());
        user.setPassword(getPassword());
        user.setNickname(getNickname());
        user.setState(getState() == null ? 0 : getState());
        user.setEmail(getEmail());
        user.setPhone(getPhone());
        user.setAvatar(getAvatar());
        user.setDeptId(getDeptId());
        return user;
    }
}
