// src/main/java/com/example/demo/security/JwtTokenProviderImpl.java

package com.example.demo.security;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.example.demo.enums.TokenType;
import com.example.demo.model.security.Token;
import com.example.demo.model.security.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ✅ ИСПРАВЛЕНО: добавляем роль с префиксом ROLE_
    @Override
    public Token generateAccessToken(Map<String, Object> extraClaims, long duration, TemporalUnit durationType, UserDetails userDetails) {
        String username = userDetails.getUsername();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plus(duration, durationType);

        // ✅ Добавляем роль в claims
        User user = (User) userDetails;
        String role = "ROLE_" + user.getRole().name(); // Например: ROLE_ADMIN

        String token = Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(username)
            .claim("roles", Collections.singletonList(role)) // ← КЛЮЧЕВОЕ ИЗМЕНЕНИЕ
            .setIssuedAt(toDate(now))
            .setExpiration(toDate(expiryDate))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();

        return Token.builder()
            .type(TokenType.ACCESS)
            .value(token)
            .expiryDate(expiryDate)
            .revoked(false)
            .expired(false)
            .user(user)
            .build();
    }

    @Override
    public Token generateRefreshToken(long duration, TemporalUnit durationType, UserDetails userDetails) {
        String username = userDetails.getUsername();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plus(duration, durationType);
        String token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(toDate(now))
            .setExpiration(toDate(expiryDate))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();

        User user = (User) userDetails;

        return Token.builder()
            .type(TokenType.REFRESH)
            .value(token)
            .expiryDate(expiryDate)
            .revoked(false)
            .expired(false)
            .user(user)
            .build();
    }

    @Override
    public boolean validateToken(String tokenValue) {
        if (tokenValue == null) return false;
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(tokenValue);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String tokenValue) {
        return extractClaim(tokenValue, Claims::getSubject);
    }

    @Override
    public LocalDateTime getExpiryDateFromToken(String tokenValue) {
        return toLocalDateTime(extractClaim(tokenValue, Claims::getExpiration));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }
}