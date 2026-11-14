package com.zqqiliyc.module.svc.system.service;

import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.module.svc.system.dto.user.SysUserRegisterDto;

/**
 * 用户注册服务，当前只支持邮箱注册：用户提交邮箱、密码、昵称与验证码，用户名与手机号由系统生成
 */
public interface ISysUserRegisterService {

    /**
     * 执行注册：校验注册方式与邮箱格式，生成用户名与手机号，校验验证码后建用户
     *
     * @param sysUserRegisterDto 注册信息，字段定义见 {@link SysUserRegisterDto}
     * @throws ClientException 注册方式不支持、邮箱为空或格式非法、验证码无效、用户已存在时抛出
     */
    void register(SysUserRegisterDto sysUserRegisterDto);

}
