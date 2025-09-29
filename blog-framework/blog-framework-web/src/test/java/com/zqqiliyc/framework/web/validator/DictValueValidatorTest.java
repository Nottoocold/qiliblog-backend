package com.zqqiliyc.framework.web.validator;

import com.zqqiliyc.framework.web.constant.DictItem;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author qili
 * @date 2025-09-29
 */
class DictValueValidatorTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    @Test
    void whenValidStatus_thenNoViolations() {
        RequestDTO dto = new RequestDTO();
        dto.setStatus(1); // 有效值

        Set<ConstraintViolation<RequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenInvalidStatus_thenViolationExists() {
        RequestDTO dto = new RequestDTO();
        dto.setStatus(99); // 无效值
        dto.setName("zqqiliyc");

        Set<ConstraintViolation<RequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}

@Getter
@Setter
class RequestDTO {
    @DictValue(enumClass = StatusEnum.class)
    private Integer status;

    @DictValue(enumClass = StatusEnum.class)
    private String name;
}

@Getter
enum StatusEnum implements DictItem<StatusEnum> {
    ENABLED(1), DISABLED(0);

    private final int value;

    StatusEnum(int value) {
        this.value = value;
    }

    @Override
    public int intVal() {
        return value;
    }
}