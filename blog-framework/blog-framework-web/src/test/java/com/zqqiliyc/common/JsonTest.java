package com.zqqiliyc.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @author qili
 * @date 2025-07-14
 */
public class JsonTest {
    private static ObjectMapper mapper;

    @BeforeAll
    public static void setup() {
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

    @Test
    public void test0() throws JsonProcessingException {
        // 创建嵌套的JSON结构
        ObjectNode order = JsonNodeFactory.instance.objectNode();
        order.put("orderId", "ORD-12345");

        // 添加嵌套对象
        ObjectNode customer = order.putObject("customer");
        customer.put("name", "王五");
        customer.put("email", "wangwu@example.com");

        // 添加数组
        ArrayNode items = order.putArray("items");
        items.addObject()
                .put("productId", "P1001")
                .put("quantity", 2);
        items.addObject()
                .put("productId", "P1002")
                .put("quantity", 1);

        System.out.println(order.toPrettyString());
        System.out.println(mapper.treeToValue(order, ObjectNode.class));
    }

    @Test
    public void test1() throws JsonProcessingException {
        TestBean bean = getBean();
        System.out.println(mapper.writeValueAsString(bean));
    }

    private TestBean getBean() {
        TestBean bean = new TestBean();
        bean.setStr("strvalue");
        bean.setBigLong(1234567890123456789L);
        bean.setSmallLong(123456789089L);
        bean.setDate(new Date());
        bean.setLocalDateTime(LocalDateTime.now());
        bean.setLocalDate(LocalDate.now());
        bean.setYearMonth(YearMonth.now());
        bean.setYear(Year.now());
        bean.setMonthDay(MonthDay.now());
        return bean;
    }

    @Getter
    @Setter
    private static class TestBean implements Serializable {
        private String str;
        private Long bigLong;
        private long smallLong;
        private Date date;
        private LocalDateTime localDateTime;
        private LocalDate localDate;
        private YearMonth yearMonth;
        private Year year;
        private MonthDay monthDay;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TestBean bean)) return false;
            return getSmallLong() == bean.getSmallLong()
                    && Objects.equals(getStr(), bean.getStr())
                    && Objects.equals(getBigLong(), bean.getBigLong())
                    && Objects.equals(getDate(), bean.getDate())
                    && Objects.equals(getLocalDateTime(), bean.getLocalDateTime())
                    && Objects.equals(getLocalDate(), bean.getLocalDate())
                    && Objects.equals(getYearMonth(), bean.getYearMonth())
                    && Objects.equals(getYear(), bean.getYear())
                    && Objects.equals(getMonthDay(), bean.getMonthDay());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getStr(), getBigLong(), getSmallLong(),
                    getDate(), getLocalDateTime(), getLocalDate(),
                    getYearMonth(), getYear(), getMonthDay());
        }
    }
}
