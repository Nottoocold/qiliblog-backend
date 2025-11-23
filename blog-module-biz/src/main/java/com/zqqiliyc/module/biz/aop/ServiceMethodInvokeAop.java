package com.zqqiliyc.module.biz.aop;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author qili
 * @date 2025-10-05
 */
@Slf4j
@Aspect
@Component
class ServiceMethodInvokeAop {
    private final Validator validator;

    public ServiceMethodInvokeAop(Validator validator) {
        this.validator = validator;
    }

    @Pointcut("execution(* com.zqqiliyc.biz.core.service..*Service.create*(..)) || " +
            "execution(* com.zqqiliyc.biz.core.service..*Service.update*(..))")
    public void serviceCreateOrUpdatePointcut() {
    }

    @Before(value = "serviceCreateOrUpdatePointcut()")
    public void beforeCreateOrUpdate(JoinPoint joinPoint) {
        // 执行约束校验
        Object[] args = joinPoint.getArgs();
        if (null != args) {
            for (Object arg : args) {
                Set<ConstraintViolation<Object>> violationSet = validator.validate(arg);
                if (!violationSet.isEmpty()) {
                    log.warn("{}#{}() is invoked, but args is invalid, args: {}, violations: {}",
                            joinPoint.getSignature().getDeclaringType().getName(), joinPoint.getSignature().getName(),
                            args, violationSet);
                    for (ConstraintViolation<Object> violation : violationSet) {
                        Path propertyPath = violation.getPropertyPath();
                        Object invalidValue = violation.getInvalidValue();
                        String message = violation.getMessage();
                        log.warn("occur constraintViolation exception: propertyPath={}, invalidValue={}, message={}",
                                propertyPath, invalidValue, message);
                    }
                    throw new ConstraintViolationException(violationSet);
                }
            }
        }
    }
}
