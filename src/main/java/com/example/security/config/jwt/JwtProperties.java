package com.example.security.config.jwt;

public interface JwtProperties {
    String SECRET = "java";
    Integer EXPIRE_TIME = 1000 * 60 * 60; //현재시간으로부터 1시간
    Integer ACCESS_TOKEN_VALIDATION_TIME = 1000 * 60 * 10; //10분
    Integer REFRESH_TOKEN_VALIDATION_TIME = 1000 * 60 * 60 * 24 * 7; //일주일

    String ACCESS_TOKEN_NAME = "accessToken";
    String TOKEN_PRIFIX = "Bearer ";
    String TOKEN_HAEDER = "Authorization";

}
