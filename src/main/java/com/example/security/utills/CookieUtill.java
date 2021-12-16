package com.example.security.utills;

import com.example.security.config.jwt.JwtProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtill {

    public Cookie createNullCookie(String cookieName){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);// Don't set to -1 or it will become a session cookie!
        cookie.setPath("/");
        cookie.setSecure(true);
        return cookie;
    }

    public Cookie createCookie(String cookieName, String value){
        Cookie cookie = new Cookie(cookieName,value);
        cookie.setHttpOnly(true); //자바스크립트 공격으로부터 안전
        cookie.setMaxAge(JwtProperties.ACCESS_TOKEN_VALIDATION_TIME);
        cookie.setPath("/");
        cookie.setSecure(true);
        return cookie;
    }

    public Cookie getCookie(HttpServletRequest req, String cookieName){
        final Cookie[] cookies = req.getCookies();
        if(cookies==null) return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }
}
