// src/main/java/com/example/demo/util/CookieUtil.java
package com.example.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${JWT_ACCESS_COOKIE_NAME:access_token}")
    private String accessTokenCookieName;

    @Value("${JWT_REFRESH_COOKIE_NAME:refresh_token}")
    private String refreshTokenCookieName;

    /**
     * Создаёт строку Set-Cookie для access_token
     */
    public String createAccessTokenCookie(String accessToken, long maxAgeSeconds) {
        ResponseCookie cookie = ResponseCookie.from(accessTokenCookieName, accessToken)
            .maxAge(maxAgeSeconds)
            .httpOnly(true)
            .secure(false)      // false для localhost (без HTTPS)
            .path("/")
            .sameSite("Lax")
            .build();
        return cookie.toString();
    }

    /**
     * Создаёт строку Set-Cookie для refresh_token
     */
    public String createRefreshTokenCookie(String refreshToken, long maxAgeSeconds) {
        ResponseCookie cookie = ResponseCookie.from(refreshTokenCookieName, refreshToken)
            .maxAge(maxAgeSeconds)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .sameSite("Lax")
            .build();
        return cookie.toString();
    }

    /**
     * Создаёт строку Set-Cookie для удаления access_token
     */
    public String deleteAccessTokenCookie() {
        ResponseCookie cookie = ResponseCookie.from(accessTokenCookieName, "")
            .maxAge(0)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .build();
        return cookie.toString();
    }

    /**
     * Создаёт строку Set-Cookie для удаления refresh_token
     */
    public String deleteRefreshTokenCookie() {
        ResponseCookie cookie = ResponseCookie.from(refreshTokenCookieName, "")
            .maxAge(0)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .build();
        return cookie.toString();
    }
}