package com.example.knife4j;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
@SpringBootApplication
public class Knife4jApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(Knife4jApplication.class, args).getEnvironment();
        log.info("Knife4j 接口文档地址: http://localhost:{}/doc.html", env.getProperty("server.port"));
    }
}
