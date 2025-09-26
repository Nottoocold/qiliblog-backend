package com.zqqiliyc.framework.web.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author qili
 * @date 2025-09-24
 */
public abstract class BaseJacksonConfig {

    protected Module javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
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

    protected Module simpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        // 序列化long类型为字符串
        simpleModule.addSerializer(Long.class, new ToStringSerializer());
        simpleModule.addSerializer(long.class, new ToStringSerializer());
        return simpleModule;
    }

    /*protected Jackson2JsonRedisSerializer<Object> createJacksonSerializerForCacheUse(ObjectMapper objectMapper) {
        ObjectMapper copiedMapper = objectMapper.copy();
        // 指定要序列化的域、getter/setter 以及修饰符范围，ANY 是包括 private 和 public
        copiedMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 激活默认类型，类型校验宽松，
        //copiedMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                // 允许所有集合的基类型
                .allowIfBaseType(Collection.class)
                // 允许所有映射的基类型
                .allowIfBaseType(Map.class)
                // 允许所有数组的基类型
                .allowIfBaseType(Object[].class)
                // 允许本项目下的所有类
                .allowIfSubType("com.zqqiliyc")
                .allowIfSubType("java.util")
                .allowIfSubType("java.lang")
                .allowIfSubType("com.fasterxml.jackson.databind.node")
                .allowIfSubType(Node.class)
                .build();
        copiedMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);
        return new Jackson2JsonRedisSerializer<>(copiedMapper, Object.class);
    }*/
}
