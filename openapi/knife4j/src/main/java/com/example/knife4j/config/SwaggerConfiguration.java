package com.example.knife4j.config;


import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration {

    /**
     *
     */
    // @Bean
    // public OpenAPI openAPI() {
    //     final String securityName = "Token";
    //     return new OpenAPI()
    //             .info(new Info()
    //                     .title("Knife4j 项目接口文档") // 文档标题
    //                     .description("项目接口文档描述") // 文档描述
    //                     .version("1.0.0") // API 版本
    //                     .termsOfService("https://www.example.com") // 服务条款
    //                     .contact(new Contact()
    //                             .name("作者名") // 联系人
    //                             .email("email@example.com") // 邮箱
    //                             .url("https://www.example.com")) // 项目地址
    //                     .license(new License()
    //                             .name("Apache 2.0") // 许可证名称
    //                             .url("https://www.apache.org/licenses/LICENSE-2.0")))
    //             .components(new Components()
    //                     .addSecuritySchemes(securityName,
    //                             new SecurityScheme()
    //                                     .name(securityName)
    //                                     .type(SecurityScheme.Type.HTTP)
    //                                     .scheme("bearer")
    //                                     .bearerFormat("JWT")))
    //             .addSecurityItem(new SecurityRequirement().addList(securityName)); // 许可证链接
    // }
    @Bean
    public OpenAPI openAPI(SwaggerProperties properties) {
        final String securityName = "Token";
        // OpenAPI info = new OpenAPI()
        //         .info(buildInfo(properties));
        // info.getComponents().addSecuritySchemes(securityName,
        //         new SecurityScheme()
        //                 .name(securityName)
        //                 .type(SecurityScheme.Type.HTTP)
        //                 .scheme("bearer")
        //                 .bearerFormat("JWT"));
        // info.addSecurityItem(new SecurityRequirement().addList(securityName));
        // return info;
        return new OpenAPI()
                .info(buildInfo(properties))
                .components(new Components()
                        .addSecuritySchemes(securityName, new SecurityScheme()
                                .name(securityName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securityName));
    }

    private Info buildInfo(SwaggerProperties properties) {
        return new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(new Contact().name(properties.getAuthor()).url(properties.getUrl()).email(properties.getEmail()))
                .license(new License().name(properties.getLicense()).url(properties.getLicenseUrl()));
    }

    @Bean
    public GroupedOpenApi allGroupedOpenApi() {
        return buildGroupedOpenApi("all", "");
    }

    @Bean
    public GroupedOpenApi exampleApi() {
        final String securityName = "Token";
        return GroupedOpenApi.builder()
                .group("示例分组") // 分组名称
                // .packagesToScan("com.example.controller") // 包路径
                .pathsToMatch("/example/**")
                .addOpenApiCustomizer(openApi -> openApi
                        .components(new Components()
                                .addSecuritySchemes(securityName, new SecurityScheme()
                                        .name(securityName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                        .addSecurityItem(new SecurityRequirement()
                                .addList(securityName)))
                .build();
    }

    // @Bean
    // public GroupedOpenApi exampleApi() {
    //     final String securityName = "Token";
    //     return GroupedOpenApi.builder()
    //             .group("示例分组") // 分组名称
    //             // .packagesToScan("com.example.controller") // 包路径
    //             .pathsToMatch("/example/**")
    //             .addOpenApiCustomizer(openApi -> openApi
    //                     .components(new Components()
    //                             .addSecuritySchemes(securityName, new SecurityScheme()
    //                                     .name(securityName)
    //                                     .type(SecurityScheme.Type.HTTP)
    //                                     .scheme("bearer")
    //                                     .bearerFormat("JWT")))
    //                     .addSecurityItem(new SecurityRequirement()
    //                             .addList(securityName)))
    //             .addOperationCustomizer((operation, handlerMethod) -> {
    //                 operation.addParametersItem(new Parameter()
    //                         .name("token")
    //                         .description("用户认证 Token")
    //                         .in(ParameterIn.HEADER.toString())
    //                         .schema(new StringSchema()._default("default-token"))
    //                         .required(true)); // 表示必输
    //                 return operation;
    //             })
    //             .build();
    // }

    public static GroupedOpenApi buildGroupedOpenApi(String group, String path) {
        return GroupedOpenApi.builder()
                .group(group)
                .pathsToMatch("/" + path + "/**")
                .addOperationCustomizer((operation, handlerMethod) -> operation
                        .addParametersItem(buildTokenHeaderParameter()))
                .build();
    }

    private static Parameter buildTokenHeaderParameter() {
        return new Parameter()
                .name("token")
                .description("用户认证 Token")
                .in(ParameterIn.HEADER.toString())
                .schema(new StringSchema()._default("default-token"))
                .required(false);
    }
}
