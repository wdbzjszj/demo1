package com.stu8317.demo1.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stu8317.demo1.entity.User;
import com.stu8317.demo1.mapper.UserMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 读取请求头中的Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. 如果没有Authorization，或者不是Bearer开头，直接放行给后续过滤器
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 截取真正的JWT字符串
        String jwt = authHeader.substring(7);
        String username;

        try {
            // 4. 从JWT中解析用户名
            username = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            // Token解析失败，直接放行
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 解析到了用户名，且当前还没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 6. 根据用户名查数据库，确认用户存在
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, username);
            User dbUser = userMapper.selectOne(queryWrapper);

            if (dbUser != null && jwtUtil.isTokenValid(jwt, dbUser.getUsername())) {
                // 7. 构造Spring Security认可的UserDetails对象
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                        .withUsername(dbUser.getUsername())
                        .password(dbUser.getPassword())
                        .authorities("ROLE_USER")
                        .build();

                // 8. 创建认证对象
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 9. 绑定当前请求的详细信息
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 10. 放入SecurityContext，表示当前请求已认证
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 11. 继续执行后续过滤器
        filterChain.doFilter(request, response);
    }
}