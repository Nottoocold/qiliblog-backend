package com.zqqiliyc;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.function.Supplier;

/**
 * @author qili
 * @date 2025-11-23
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = TestServiceApp.class, properties = "spring.profiles.active=test")
public class BaseTestApp {
    @Autowired
    protected ApplicationContext applicationContext;

    protected static String generateString(int loop, Supplier<String> stringSupplier) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < loop; i++) {
            builder.append(stringSupplier.get());
        }
        return builder.toString();
    }
}
