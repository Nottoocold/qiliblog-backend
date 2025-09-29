package com.zqqiliyc.framework.web.validator;

import cn.hutool.core.convert.Convert;
import com.zqqiliyc.framework.web.constant.DictItem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qili
 * @date 2025-09-29
 */
@Slf4j
public class DictValueValidator implements ConstraintValidator<DictValue, Object> {
    private Class<? extends DictItem<?>> enumClass;

    @Override
    public void initialize(DictValue constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Integer val = Convert.toInt(value);
        DictItem<?>[] items = enumClass.getEnumConstants();
        for (DictItem<?> item : items) {
            Enum<?> o = item.fromInt(val);
            if (o != null) {
                // 说明找到了对应的枚举对象
                return true;
            }
        }
        return false;
    }
}
