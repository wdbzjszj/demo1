package com.stu8317.demo1.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-millis}")
    private long expirationMillis;

    // 根据配置文件生成签名密钥
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 生成JWT
    public String generateToken(String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(username)       // 主题：用户名
                .issuedAt(now)           // 签发时间
                .expiration(expireDate)  // 过期时间
                .signWith(getSignKey())  // 签名
                .compact();
    }

    // 解析JWT获取Claims
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 从JWT中提取用户名
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // 校验Token是否有效（未过期、用户名匹配）
    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // 校验Token是否过期
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}