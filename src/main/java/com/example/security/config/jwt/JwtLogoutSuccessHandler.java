package com.example.security.config.jwt;

import com.example.security.domain.CMRespDto;
import com.example.security.utills.Script;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler{

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

      log.info("로그아웃 성공!");


      //원래는 막 여기서 저희가 발생한 jwt 강제로 만료시키는 게 좋은 데 딱히 방법이 없어요.보안 취약점

        CMRespDto cmRespDto = new CMRespDto(1, "로그아웃");


        Script.responseData(response, cmRespDto);
    }
}
