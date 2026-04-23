package com.stu8317.demo1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 开启 CORS 配置
                .cors(cors -> cors.configure(http))
                // 关闭 CSRF（前后端分离场景）
                .csrf(csrf -> csrf.disable())
                // 配置无状态会话（不使用 Session）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 接口权限规则配置
                .authorizeHttpRequests(auth -> auth
                        // 放行注册接口：POST /api/users
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        // 放行登录接口：POST /api/users/login
                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                        // 其他所有接口都需要认证
                        .anyRequest().authenticated()
                )
                // 关闭表单登录和 HTTP Basic 认证
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}