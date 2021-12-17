package com.example.security.config.jwt;

import com.example.security.domain.CMRespDto;
import com.example.security.utills.CookieUtill;
import com.example.security.utills.Script;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler{

    private final CookieUtill cookieUtill;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

      log.info("로그아웃 성공!");

        CMRespDto cmRespDto = new CMRespDto(1, null);
        Cookie accessNullToken = cookieUtill.createNullCookie(JwtProperties.ACCESS_TOKEN_NAME);

        response.addCookie(accessNullToken);

        Script.responseData(response, cmRespDto);
    }
}
