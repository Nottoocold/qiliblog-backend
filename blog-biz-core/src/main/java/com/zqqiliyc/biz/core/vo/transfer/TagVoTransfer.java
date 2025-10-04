package com.zqqiliyc.biz.core.vo.transfer;

import com.zqqiliyc.biz.core.dto.ViewVoTransfer;
import com.zqqiliyc.biz.core.entity.Tag;
import com.zqqiliyc.biz.core.vo.TagVo;
import org.springframework.stereotype.Component;

/**
 * @author qili
 * @date 2025-09-30
 */
@Component
public class TagVoTransfer implements ViewVoTransfer<Tag, TagVo> {

    @Override
    public TagVo newInstance() {
        return new TagVo();
    }
}
