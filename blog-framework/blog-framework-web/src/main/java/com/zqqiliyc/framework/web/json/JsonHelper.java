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

    public static JsonHelper getInstance(ObjectMapper mapper) {
        JsonHelper.mapper = mapper;
        return new JsonHelper();
    }

    public static ObjectMapper newMapper() {
        // Jackson 3 的 ObjectMapper 不可变，copy() 由 rebuild().build() 替代
        return mapper.rebuild().build();
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