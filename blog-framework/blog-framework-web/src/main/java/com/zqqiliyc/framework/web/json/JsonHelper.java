package com.zqqiliyc.framework.web.json;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author qili
 * @date 2025-07-15
 */
public class JsonHelper {
    private static ObjectMapper mapper;

    private JsonHelper() {

    }

    /**
     * 由 Spring 配置在 ObjectMapper 就绪后调用一次；
     * PostgreSQLJsonTypeHandler 等非 Spring 组件通过静态方法使用本门面
     */
    public static void init(ObjectMapper mapper) {
        JsonHelper.mapper = mapper;
    }


    public static String toJson(Object obj) {
        return mapper.writeValueAsString(obj);
    }

    public static byte[] toBytes(Object obj) {
        return mapper.writeValueAsBytes(obj);
    }

    public static void toJson(Object obj, OutputStream os) {
        mapper.writeValue(os, obj);
    }

    public static <T> T fromJson(String content, Class<T> clazz) {
        return mapper.readValue(content, clazz);
    }

    public static <T> T fromJson(String content, TypeReference<T> typeReference) {
        return mapper.readValue(content, typeReference);
    }

    public static <T> T fromJson(InputStream is, Class<T> clazz) {
        return mapper.readValue(is, clazz);
    }

    public static <T> T fromJson(InputStream is, TypeReference<T> typeReference) {
        return mapper.readValue(is, typeReference);
    }
}