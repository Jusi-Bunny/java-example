package com.example.satoken.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    // 注册拦截器
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验
    //     registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
    //             .addPathPatterns("/**")
    //             .excludePathPatterns("/auth/login");
    // }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 指定一条 match 规则
                    SaRouter
                            .match("/**") // 拦截的 path 列表，可以写多个 */
                            .notMatch("/auth/login") // 排除掉的 path 列表，可以写多个
                            .check(r -> {
                                StpUtil.checkLogin();
                                StpUtil.checkDisable(StpUtil.getLoginId());
                            }); // 要执行的校验动作，可以写完整的 lambda 表达式
                }))
                .addPathPatterns("/**");
    }
}
