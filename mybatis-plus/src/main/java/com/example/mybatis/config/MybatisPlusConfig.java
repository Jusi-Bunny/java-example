package com.example.mybatis.config;

// @Configuration
// public class MybatisPlusConfig {
//
//     @Bean
//     public IKeyGenerator keyGenerator() {
//         return new PostgreKeyGenerator();
//     }
//
//     @Bean
//     public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
//         return plusProperties -> plusProperties
//                 .getGlobalConfig()
//                 .getDbConfig()
//                 .setKeyGenerators(List.of(new PostgreKeyGenerator()));
//     }
//
//     @Bean
//     public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
//         return plusProperties -> plusProperties
//                 .getGlobalConfig()
//                 .setIdentifierGenerator(new CustomIdGenerator());
//     }
// }