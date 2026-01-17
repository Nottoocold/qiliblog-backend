package com.zqqiliyc.module.svc.main.domain.dto.tag;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.framework.web.domain.dto.CreateDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-30
 */
@Getter
@Setter
@Schema(description = "标签创建参数")
public class TagCreateDTO implements CreateDTO<Tag> {
    @NotBlank(message = "标签名不能为空")
    @Schema(description = "标签名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "标签友好标识符不能为空")
    @Schema(description = "标签 URL 友好标识符 (必须唯一，如 java, spring-boot)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String slug;

    @Schema(description = "标签描述")
    private String description;

    @Override
    public Tag toEntity() {
        Tag entity = new Tag();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
