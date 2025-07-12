package com.zqqiliyc.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zqqiliyc.admin.RegisterResult;
import com.zqqiliyc.admin.dto.UserRegisterDto;
import com.zqqiliyc.admin.service.IRegisterService;
import com.zqqiliyc.admin.strategy.RegistrationStrategy;
import com.zqqiliyc.common.enums.AuthState;
import com.zqqiliyc.common.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 注册服务接口，定义用户注册的基本行为契约。
 *
 * @author hallo
 * @datetime 2025-07-01 11:34
 * @description
 *     提供统一的注册入口，支持多种注册方式（如手机号、邮箱、第三方）。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService implements IRegisterService {

    private static final String LOG_PREFIX = "[RegisterService] ";

    private final List<RegistrationStrategy> registrationStrategyList;


    /**
     * 用户注册（支持多种注册方式）
     * <p>
     * 查找匹配的注册策略，若未找到则抛出异常
     *
     * @param userRegisterDto 注册信息
     * @return 注册结果
     * @throws AuthException 如果注册类型为空或不被支持
     */
    @Override
    public RegisterResult register(UserRegisterDto userRegisterDto) {
        if (userRegisterDto.getRegisterType() == null) {
            log.warn("{}注册类型为空", LOG_PREFIX);
            throw new AuthException(AuthState.REGISTER_TYPE_EMPTY);
        }
        RegistrationStrategy strategy = CollectionUtil.findOne(
                registrationStrategyList,
                s -> s.support(userRegisterDto.getRegisterType()));

        if (null == strategy) {
            log.warn("{}不支持的注册类型: {}", LOG_PREFIX, userRegisterDto.getRegisterType());
            throw new AuthException(AuthState.UNSUPPORTED_REGISTER_TYPE);
        }
        return strategy.register(userRegisterDto);
    }
}
