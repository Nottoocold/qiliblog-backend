package com.zqqiliyc.framework.web.validator;

import com.zqqiliyc.framework.web.constant.DictItem;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author qili
 * @date 2025-09-29
 */
@Documented
@Constraint(validatedBy = {DictValueValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface DictValue {

    // 默认错误消息（支持SpEL表达式）
    String message() default "无效的枚举值";

    // 分组校验（可选）
    Class<?>[] groups() default {};

    // 负载信息（可选）
    Class<? extends Payload>[] payload() default {};

    // 指定目标枚举类
    Class<? extends DictItem<?>> enumClass();
}
