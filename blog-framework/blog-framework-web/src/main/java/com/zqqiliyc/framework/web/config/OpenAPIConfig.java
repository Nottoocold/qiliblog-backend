package com.zqqiliyc.framework.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

/**
 * @author qili
 * @date 2025-09-13
 */
@Profile("dev")
@Configuration
public class OpenAPIConfig {

    @Value("${server.port}")
    protected int port = 8080;

    @Bean
    public OpenAPI openAPI() {
        // 定义测试环境服务器URL
        String devUrl = "http://localhost:" + port;
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("测试环境的服务器URL");

        // 定义生产环境服务器URL
        String prodUrl = "http://prod.example";
        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("正式环境的服务器URL");

        // 定义联系人对象
        Contact contact = new Contact();
        contact.setEmail("qilizhiwai@blog.com");
        contact.setName("七里之外");
        contact.setUrl("https://www.example.com");

        License mitLicense = new License().name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        // 定义API信息对象
        Info info = new Info()
                .title("Blog API")
                .version("1.0")
                .contact(contact)
                .description("对外API接口说明.")
                .termsOfService("https://www.sanfate.com/terms")
                .license(mitLicense);

        // 返回OpenAPI对象，包含API信息和服务器信息
        return new OpenAPI().info(info)
                .servers(List.of(devServer, prodServer));
    }
}
