package com.zqqiliyc.framework.web.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
