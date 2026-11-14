package com.zqqiliyc.framework.web.json;

import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.ext.javatime.deser.*;
import tools.jackson.databind.ext.javatime.ser.*;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 模块工具：自定义时间格式与 Long 字符串序列化
 */
public final class JacksonModules {

    private JacksonModules() {
    }

    /**
     * Jackson 3 已内置 java.time 支持，此模块仅用于覆盖为自定义格式
     */
    public static JacksonModule javaTimeModule() {
        SimpleModule javaTimeModule = new SimpleModule();
        // jdk8时间类型序列化器
        DateTimeFormatter f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter f3 = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter f4 = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter f5 = DateTimeFormatter.ofPattern("MM-dd");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(f1));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(f2));
        javaTimeModule.addSerializer(YearMonth.class, new YearMonthSerializer(f3));
        javaTimeModule.addSerializer(Year.class, new YearSerializer(f4));
        javaTimeModule.addSerializer(MonthDay.class, new MonthDaySerializer(f5));
        // jdk8时间类型反序列化器
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(f1));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(f2));
        javaTimeModule.addDeserializer(YearMonth.class, new YearMonthDeserializer(f3));
        javaTimeModule.addDeserializer(Year.class, new YearDeserializer(f4));
        javaTimeModule.addDeserializer(MonthDay.class, new MonthDayDeserializer(f5));
        return javaTimeModule;
    }

    /**
     * Long 序列化为字符串，避免前端精度丢失
     */
    public static JacksonModule simpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        // 序列化long类型为字符串
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(long.class, ToStringSerializer.instance);
        return simpleModule;
    }
}