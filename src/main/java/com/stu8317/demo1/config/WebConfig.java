package com.stu8317.demo1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 核心机房配置注解，极度重要
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new com.stu8317.demo1.intercepter.AuthInterceptor())
                .addPathPatterns("/api/**") // 拦截 /api 下的所有请求路径
                .excludePathPatterns(
                        "/api/users/login"//,  // 放行：保留原本放行的登录接口
                        //"/api/users",        // 放行：新增用户接口
                        //"/api/users/{id}"    // 放行：获取用户信息接口（路径匹配 {id}）
                );
    }
}