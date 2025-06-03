package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.Tag;
import com.zqqiliyc.mapper.TagMapper;
import com.zqqiliyc.service.ITagService;
import com.zqqiliyc.service.base.AbstractExtendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagService extends AbstractExtendService<Tag, Long, TagMapper> implements ITagService {
}
