package com.example.security.config.jwt;

public interface JwtProperties {
    String SECRET = "java";
    Integer EXPIRE_TIME = 1000 * 60 * 60; //1시간
    String TOKEN_PRIFIX = "Bearer ";
    String TOKEN_HAEDER = "Authorization";


    Integer REFRESH_TOKEN_VALIDATION_TIME = 1000 * 60 * 60 * 24 * 7; //일주일
    String REFRESH_TOKEN_NAME = "refreshToken";
}
