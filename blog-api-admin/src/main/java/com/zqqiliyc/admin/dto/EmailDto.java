package com.zqqiliyc.admin.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据传输对象，用于封装邮箱相关信息。
 *
 * <p>该类通常用于接收前端传递的邮箱数据，并进行格式校验。</p>
 *
 * @author hallo
 * @datetime 2025-07-01 18:18
 */
@Getter
@Setter
public class EmailDto {

    /**
     * 用户的电子邮箱地址。
     * <p>必须符合标准的邮箱格式，例如：example@example.com。</p>
     */
    @Email(message = "邮箱格式不正确")
    private String email;

}