package com.zqqiliyc.biz.core.vo.transfer;

import com.zqqiliyc.biz.core.dto.ViewVoTransfer;
import com.zqqiliyc.biz.core.entity.Category;
import com.zqqiliyc.biz.core.vo.CategoryVo;
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
