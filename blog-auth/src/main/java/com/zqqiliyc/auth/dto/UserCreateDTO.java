package com.zqqiliyc.auth.dto;

import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.entity.SysUser;
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
public class UserCreateDTO implements CreateDto<SysUser> {

    /**
     * 登录用户名，必须唯一，长度限制建议在4~20位之间
     */
    private String username;

    /**
     * 登录密码，必须为 SHA256 加密后的字符串
     */
    private String password;

    /**
     * 用户昵称，用于展示，默认与用户名一致
     */
    private String nickname;

    /**
     * 用户状态，0 表示正常，1 表示禁用
     */
    private Integer state;

    /**
     * 邮箱地址，用于找回密码或通知，可为空
     */
    private String email;

    /**
     * 手机号码，可为空，建议中国大陆手机号格式
     */
    private String phone;

    /**
     * 用户头像图片的 URL 地址，可为空
     */
    private String avatar;

    /**
     * 所属部门 ID，用于权限管理，默认为 -1（未分配）
     */
    private Long deptId;

    /**
     * 将当前 DTO 转换为对应的实体对象 SysUser
     *
     * <p>该方法通常用于将用户输入数据映射到数据库实体进行保存。</p>
     *
     * @return 返回构建完成的 SysUser 实体对象
     */
    @Override
    public SysUser toEntity() {
        SysUser user = new SysUser();
        user.setUsername(getUsername());
        user.setPassword(getPassword());
        user.setNickname(getNickname());
        user.setState(getState());
        user.setEmail(getEmail());
        user.setPhone(getPhone());
        user.setAvatar(getAvatar());
        user.setDeptId(getDeptId());
        return user;
    }
}
