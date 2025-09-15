package com.example.knife4j.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Knife4j 项目接口文档") // 文档标题
                        .description("项目接口文档描述") // 文档描述
                        .version("1.0.0") // API 版本
                        .contact(new Contact()
                                .name("作者名") // 联系人
                                .email("email@example.com") // 邮箱
                                .url("https://www.example.com")) // 项目地址
                        .license(new License()
                                .name("Apache 2.0") // 许可证名称
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))); // 许可证链接
    }

    @Bean
    public GroupedOpenApi exampleApi() {
        return GroupedOpenApi.builder()
                .group("示例接口") // 分组名称
                // .packagesToScan("com.example.controller") // 包路径
                .pathsToMatch("/example/**")
                .build();
    }
}
