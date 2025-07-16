package com.zqqiliyc.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author qili
 * @date 2025-07-15
 */
public class JsonHelper {
    private static ObjectMapper mapper;

    static {
        setup();
    }

    private JsonHelper() {

    }

    private static void setup() {
        mapper = new ObjectMapper();
        // 忽略对象字段值为null的字段
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化没有属性的对象时不报错
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 反序列化时忽略未知字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

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

        SimpleModule simpleModule = new SimpleModule();
        // 序列化long类型为字符串
        simpleModule.addSerializer(Long.class, new ToStringSerializer());
        simpleModule.addSerializer(long.class, new ToStringSerializer());

        // 注册模块
        mapper.registerModules(simpleModule, new Jdk8Module(), javaTimeModule);

        // 格式化序列化后的字符串
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
    }

    public static ObjectMapper newMapper() {
        return mapper.copy();
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toBytes(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void toJson(Object obj, OutputStream os) {
        try {
            mapper.writeValue(os, obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String content, Class<T> clazz) {
        try {
            return mapper.readValue(content, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String content, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(content, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(InputStream is, Class<T> clazz) {
        try {
            return mapper.readValue(is, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(InputStream is, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(is, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
