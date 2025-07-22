package com.zqqiliyc.service.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.dto.QueryDto;
import com.zqqiliyc.domain.dto.UpdateDto;
import com.zqqiliyc.domain.entity.BaseEntity;
import com.zqqiliyc.framework.web.bean.PageResult;
import io.mybatis.mapper.BaseMapper;
import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.example.ExampleWrapper;
import io.mybatis.mapper.fn.Fn;
import io.mybatis.provider.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author qili
 * @date 2025-06-03
 */
public abstract class AbstractBaseService<T extends BaseEntity, I extends Serializable, M extends BaseMapper<T, I>>
        implements IBaseService<T, I> {
    protected M baseMapper;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public void setBaseMapper(M baseMapper) {
        this.baseMapper = baseMapper;
    }

    protected ExampleWrapper<T, I> wrapper() {
        return baseMapper.wrapper();
    }

    protected Example<T> example() {
        return new Example<>();
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    @Override
    public T findById(I id) {
        return baseMapper.selectByPrimaryKey(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    @Override
    public T findOne(Example<T> example) {
        return baseMapper.selectOneByExample(example).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public PageResult<T> findPageInfo(QueryDto<T> queryDto) {
        if (queryDto.isPageRequest()) {
            try (Page<T> page = PageHelper.startPage(queryDto.getPageNum(), queryDto.getPageSize())) {
                PageInfo<T> pageInfo = page.doSelectPageInfo(() -> baseMapper.selectByExample(queryDto.toExample()));
                return PageResult.of(pageInfo);
            }
        }
        return PageResult.emptyPageResult();
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    @Override
    public List<T> findList(Example<T> example) {
        return baseMapper.selectByExample(example);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    @Override
    public long count(Example<T> example) {
        return baseMapper.countByExample(example);
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    @Override
    public <F> List<T> findByFieldList(Fn<T, F> field, Collection<F> fieldValueList) {
        return baseMapper.selectByFieldList(field, fieldValueList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public T create(CreateDto<T> dto) {
        T entity = dto.toEntity();
        Assert.isTrue(baseMapper.insert(entity) == 1, "insert failed");
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public T update(UpdateDto<T> dto) {
        T entity = findById(dto.getId());
        dto.fillEntity(entity);
        Assert.isTrue(baseMapper.updateByPrimaryKey(entity) == 1, "update failed");
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public T update(T entity) {
        Assert.isTrue(baseMapper.updateByPrimaryKey(entity) == 1, "update failed");
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public T updateSelective(T entity) {
        Assert.isTrue(baseMapper.updateByPrimaryKeySelective(entity) == 1, "update failed");
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteById(I id) {
        int count = baseMapper.deleteByPrimaryKey(id);
        Assert.isTrue(count == 1, "delete failed");
        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <F> int deleteByFieldList(Fn<T, F> field, Collection<F> fieldValueList) {
        return baseMapper.deleteByFieldList(field, fieldValueList);
    }
}
