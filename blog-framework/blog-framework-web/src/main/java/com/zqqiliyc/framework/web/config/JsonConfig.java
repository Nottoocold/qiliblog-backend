package com.zqqiliyc.framework.web.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.zqqiliyc.framework.web.json.JsonHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author qili
 * @date 2025-07-15
 */
@Configuration
public class JsonConfig {

    @Bean
    public Module javaTimeModule() {
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

    @Bean
    public Module simpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        // 序列化long类型为字符串
        simpleModule.addSerializer(Long.class, new ToStringSerializer());
        simpleModule.addSerializer(long.class, new ToStringSerializer());
        return simpleModule;
    }

    @Bean
    public JsonHelper jsonHelper(ObjectMapper mapper) {
        return JsonHelper.getInstance(mapper);
    }

    /*@Bean
    public Module jdk8Module() {
        return new com.fasterxml.jackson.datatype.jdk8.Jdk8Module();
    }*/

    /*@Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return jacksonObjectMapperBuilder -> {
            SimpleModule simpleModule = new SimpleModule();
            // 序列化long类型为字符串
            simpleModule.addSerializer(Long.class, new ToStringSerializer());
            simpleModule.addSerializer(long.class, new ToStringSerializer());

            jacksonObjectMapperBuilder.modulesToInstall(modules -> modules.addAll(List.of(simpleModule)));
        };
    }*/
}
