package com.zqqiliyc.domain.vo.transfer;

import com.zqqiliyc.domain.dto.ViewVoTransfer;
import com.zqqiliyc.domain.entity.Tag;
import com.zqqiliyc.domain.vo.TagVo;
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
