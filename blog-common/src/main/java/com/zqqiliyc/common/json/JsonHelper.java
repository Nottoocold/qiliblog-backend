package com.zqqiliyc.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author qili
 * @date 2025-07-15
 */
@Component
public class JsonHelper {
    private final ObjectMapper mapper;

    public JsonHelper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ObjectMapper newMapper() {
        return mapper.copy();
    }

    public String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] toBytes(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void toJson(Object obj, OutputStream os) {
        try {
            mapper.writeValue(os, obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String content, Class<T> clazz) {
        try {
            return mapper.readValue(content, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String content, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(content, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(InputStream is, Class<T> clazz) {
        try {
            return mapper.readValue(is, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(InputStream is, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(is, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
