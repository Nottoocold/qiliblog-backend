package com.zqqiliyc.framework.web.service;

import cn.hutool.core.lang.Assert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.domain.dto.QueryDTO;
import com.zqqiliyc.framework.web.domain.entity.BaseEntity;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import io.mybatis.mapper.BaseMapper;
import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.example.ExampleWrapper;
import io.mybatis.mapper.fn.Fn;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author qili
 * @date 2025-06-03
 */
@Slf4j
public abstract class AbstractBaseService<T extends BaseEntity, I extends Serializable, M extends BaseMapper<T, I>>
        implements IBaseService<T, I> {
    protected M baseMapper;
    @Autowired
    protected Validator validator;

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
    public PageResult<T> findPageInfo(QueryDTO<T> queryDto) {
        if (queryDto.isPageRequest()) {
            try (Page<T> page = PageHelper.startPage(queryDto.getCurrent(), queryDto.getPageSize())) {
                PageInfo<T> pageInfo = page.doSelectPageInfo(() -> baseMapper.selectByExample(queryDto.toExample()));
                return PageResult.of(pageInfo);
            }
        } else {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "分页请求参数错误");
        }
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
    public T create(T entity) {
        beforeCreate(entity);
        Assert.isTrue(baseMapper.insert(entity) == 1, "insert failed");
        afterCreate(entity);
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public T update(T entity) {
        beforeUpdate(entity);
        Assert.isTrue(baseMapper.updateByPrimaryKey(entity) == 1, "update failed");
        afterUpdate(entity);
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(I id) {
        T entity = findById(id);
        beforeDelete(entity);
        Assert.isTrue(baseMapper.deleteByPrimaryKey(id) == 1, "delete failed");
        afterDelete(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <F> int deleteByFieldList(Fn<T, F> field, Collection<F> fieldValueList) {
        return baseMapper.deleteByFieldList(field, fieldValueList);
    }

    // 钩子默认空实现，子类按需重写；调用时机见 create/update/deleteById
    protected void beforeCreate(T entity) {
    }

    protected void afterCreate(T entity) {
    }

    protected void beforeUpdate(T entity) {
    }

    protected void afterUpdate(T entity) {
    }

    protected void beforeDelete(T entity) {
    }

    protected void afterDelete(T entity) {
    }
}
