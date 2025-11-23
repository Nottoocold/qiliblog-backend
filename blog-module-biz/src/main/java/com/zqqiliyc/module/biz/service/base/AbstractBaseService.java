package com.zqqiliyc.module.biz.service.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.TypeUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.event.EntityDeleteEvent;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import com.zqqiliyc.module.biz.dto.CreateDTO;
import com.zqqiliyc.module.biz.dto.QueryDTO;
import com.zqqiliyc.module.biz.dto.UpdateDTO;
import com.zqqiliyc.module.biz.entity.BaseEntity;
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
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

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
    public T create(CreateDTO<T> dto) {
        T entity = dto.toEntity();
        Assert.isTrue(baseMapper.insert(entity) == 1, "insert failed");
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public T update(UpdateDTO<T> dto) {
        T entity = findById(dto.getId());
        dto.fillEntity(entity);
        Assert.isTrue(baseMapper.updateByPrimaryKey(entity) == 1, "update failed");
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(I id) {
        T entity = findById(id);
        SpringUtils.publishEvent(new EntityDeleteEvent<>(this, entity));
        beforeDelete(entity);
        int count = baseMapper.deleteByPrimaryKey(id);
        Assert.isTrue(count == 1, "delete failed");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <F> int deleteByFieldList(Fn<T, F> field, Collection<F> fieldValueList) {
        return baseMapper.deleteByFieldList(field, fieldValueList);
    }

    /**
     * 创建时，检查唯一性约束，这是默认行为；
     * 子类可重写该方法，进行自定义的校验
     *
     * @param conditions 查询条件
     * @param message    唯一性约束检查失败时的错误信息
     */
    protected void validateBeforeCreateWithDB(Map<Fn<T, Object>, Object> conditions, String message) {
        validateWithDB(conditions, () -> new ClientException(GlobalErrorDict.PARAM_ERROR, message), null);
    }

    /**
     * 更新时，检查唯一性约束，这是默认行为；
     * 子类可重写该方法，进行自定义的校验
     *
     * @param conditions 唯一性约束条件
     * @param message    唯一性约束检查失败时的错误信息
     * @param excludeIds 检查时需排除的实体id列表
     */
    protected void validateBeforeUpdateWithDB(Map<Fn<T, Object>, Object> conditions, String message, I[] excludeIds) {
        validateWithDB(conditions, () -> new ClientException(GlobalErrorDict.PARAM_ERROR, message), List.of(excludeIds));
    }

    @SuppressWarnings("unchecked")
    private void validateWithDB(Map<Fn<T, Object>, Object> conditions,
                                Supplier<? extends RuntimeException> supplier,
                                List<I> excludeIds) {
        Objects.requireNonNull(conditions);
        Objects.requireNonNull(supplier);
        ExampleWrapper<T, I> exampleWrapper = wrapper();
        for (Map.Entry<Fn<T, Object>, Object> entry : conditions.entrySet()) {
            exampleWrapper.eq(entry.getKey(), entry.getValue());
        }
        if (CollUtil.isEmpty(excludeIds)
                && exampleWrapper.first().isPresent()) {
            throw supplier.get();
        }
        Class<T> entityClass = (Class<T>) TypeUtil.getClass(TypeUtil.getTypeArgument(getClass(), 0));
        if (CollUtil.isNotEmpty(excludeIds)
                && exampleWrapper.notIn(Fn.field(entityClass, "id"), excludeIds).first().isPresent()) {
            throw supplier.get();
        }
    }

    protected void beforeDelete(T entity) {
    }
}
