package com.zqqiliyc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {


    @GetMapping("/index")
    public String index(String message){
        return StringUtils.hasText(message) ? message : "Ping!";
    }
}
