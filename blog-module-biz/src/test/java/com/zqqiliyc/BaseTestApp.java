package com.zqqiliyc;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Supplier;

/**
 * @author qili
 * @date 2025-11-23
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = BaseTestApp.TestConfig.class, properties = "spring.profiles.active=test")
public class BaseTestApp {

    @SpringBootApplication(scanBasePackages = "com.zqqiliyc")
    static class TestConfig {
        // 本地测试配置类，用于提供最小化的Spring Boot上下文
        public static void main(String[] args) {
            SpringApplication.run(TestConfig.class, args);
        }
    }

    protected static String generateString(int loop, Supplier<String> stringSupplier) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < loop; i++) {
            builder.append(stringSupplier.get());
        }
        return builder.toString();
    }
}
