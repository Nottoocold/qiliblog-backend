package com.zqqiliyc.admin.service;

import com.zqqiliyc.admin.RegisterResult;
import com.zqqiliyc.admin.dto.UserRegisterDto;
import com.zqqiliyc.common.exception.AuthException;

/**
 * @author hallo
 * @datetime 2025-07-01 11:35
 * @description 注册服务接口
 */
public interface IRegisterService {

    /**
     * 用户注册方法，处理不同类型的注册请求并返回认证结果。
     *
     * @param userRegisterDto 包含用户注册信息的数据传输对象，具体字段定义见 {@link UserRegisterDto}
     * @return 返回注册操作的结果，包括状态码、错误标识，具体结构见 {@link RegisterResult}
     * @throws AuthException 如果注册类型为空或不被支持时抛出异常
     */
    void register(UserRegisterDto userRegisterDto);

}
