// src/main/java/com/example/demo/security/JwtAuthFilter.java
package com.example.demo.security;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Value("${JWT_ACCESS_COOKIE_NAME:access_token}")
    private String accessTokenCookieName;

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = getJwtFromCookie(request);

        if (accessToken == null) {
            log.debug("Токен не найден в cookie: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("Найден токен в cookie: {}", accessToken.substring(0, Math.min(20, accessToken.length())) + "...");

        if (!tokenProvider.validateToken(accessToken)) {
            log.warn("Невалидный токен: {}", accessToken.substring(0, Math.min(20, accessToken.length())) + "...");
            filterChain.doFilter(request, response);
            return;
        }

        String username = tokenProvider.getUsernameFromToken(accessToken);
        if (username == null) {
            log.warn("Не удалось извлечь имя пользователя из токена");
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("Извлечено имя пользователя из токена: {}", username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
            );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("Запрос от авторизованного пользователя: {} | URI: {}", username, request.getRequestURI());

        filterChain.doFilter(request, response);
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.debug("Cookie отсутствуют в запросе: {}", request.getRequestURI());
            return null;
        }
        for (Cookie cookie : cookies) {
            if (accessTokenCookieName.equals(cookie.getName())) {
                log.debug("Найден cookie '{}' в запросе", accessTokenCookieName);
                return cookie.getValue();
            }
        }
        log.debug("cookie '{}' не найдена в запросе", accessTokenCookieName);
        return null;
    }
}