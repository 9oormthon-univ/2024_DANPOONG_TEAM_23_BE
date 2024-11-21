package com.doctorgc.doctorgrandchild.config.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {

    // 쿠키 생성
    public static void createCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt-token",token);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
