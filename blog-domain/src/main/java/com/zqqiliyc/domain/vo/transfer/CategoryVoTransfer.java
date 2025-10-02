package com.zqqiliyc.domain.vo.transfer;

import com.zqqiliyc.domain.dto.ViewVoTransfer;
import com.zqqiliyc.domain.entity.Category;
import com.zqqiliyc.domain.vo.CategoryVo;
import org.springframework.stereotype.Component;

/**
 * @author qili
 * @date 2025-10-02
 */
@Component
public class CategoryVoTransfer implements ViewVoTransfer<Category, CategoryVo> {

    @Override
    public CategoryVo newInstance() {
        return new CategoryVo();
    }
}
