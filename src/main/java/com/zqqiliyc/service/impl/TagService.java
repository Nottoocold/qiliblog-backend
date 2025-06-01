package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.Tag;
import com.zqqiliyc.mapper.TagMapper;
import com.zqqiliyc.service.ITagService;
import io.mybatis.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
public class TagService extends AbstractService<Tag, Long, TagMapper> implements ITagService {
}
