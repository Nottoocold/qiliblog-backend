package com.zqqiliyc.biz.core.dto.tag;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.biz.core.dto.UpdateDto;
import com.zqqiliyc.biz.core.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-30
 */
@Getter
@Setter
@Schema(description = "标签更新参数")
public class TagUpdateDto implements UpdateDto<Tag> {
    @NotNull(message = "标签ID不能为空")
    @Schema(description = "标签ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotBlank(message = "标签名不能为空")
    @Schema(description = "标签名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "标签友好标识符不能为空")
    @Schema(description = "标签 URL 友好标识符 (必须唯一，如 java, spring-boot)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String slug;

    @Schema(description = "标签描述")
    private String description;

    @Override
    public void fillEntity(Tag entity) {
        BeanUtil.copyProperties(this, entity, "id");
    }

    @Override
    public UpdateDto<Tag> fromEntity(Tag entity) {
        TagUpdateDto dto = new TagUpdateDto();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }
}
