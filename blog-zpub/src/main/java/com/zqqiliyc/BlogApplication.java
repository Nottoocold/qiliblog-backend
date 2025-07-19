package com.zqqiliyc;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.session.mgt.SessionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@Slf4j
@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BlogApplication.class, args);
        Map<String, Authenticator> a1 = context.getBeansOfType(Authenticator.class);
        Map<String, Authorizer> a2 = context.getBeansOfType(Authorizer.class);
        Map<String, SecurityManager> a3 = context.getBeansOfType(SecurityManager.class);
        Map<String, SubjectDAO> a4 = context.getBeansOfType(SubjectDAO.class);
        Map<String, SessionManager> a5 = context.getBeansOfType(SessionManager.class);
    }
}
