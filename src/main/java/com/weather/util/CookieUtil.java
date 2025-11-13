package com.weather.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CookieUtil {

    public Optional<String> getSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("SESSION_ID")) {
                    String sessionId = cookie.getValue();
                    if (sessionId != null && !sessionId.isBlank()) {
                        return Optional.of(sessionId);
                    }
                }
            }
        }
        return Optional.empty();
    }

    public Cookie createCookie(UUID token) {
        Cookie cookie = new Cookie("SESSION_ID", token.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        return cookie;
    }

    public void invalidateCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("SESSION_ID", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
