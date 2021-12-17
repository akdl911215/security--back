package com.example.security.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.config.auth.PrincipalDetails;
import com.example.security.config.oauth.GoogleInfo;
import com.example.security.config.oauth.OAuth2UserInfo;
import com.example.security.domain.CMRespDto;
import com.example.security.domain.LoginDto;
import com.example.security.domain.User;
import com.example.security.utills.CookieUtill;
import com.example.security.utills.Script;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Map;

//UsernamePasswordAuthenticationFilter 를 커스텀해서 갈아치웁니다.
@Slf4j
@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final CookieUtill cookieUtill;


    //시큐리티는 기본적으로 form 방식, 기존 로그인 방식을 갈아치울거에요.
    //post 방식으로 /mylogin 으로 오는 요청을 낚아채요.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("로그인 요청 옴");

        ObjectMapper objectMapper = new ObjectMapper();

        LoginDto loginDto = null;

        try {
            loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            log.info("로그인 Dto=>{}", loginDto);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("로그인 요청 dto 생성 중 실패: " + e.getMessage());

        }

        //1. UsernamePassword 토큰을 만들기
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        //authenticationManager에게 token을 전달하면, 자동으로 UserDetailsService 가 호출되요. => 응답을 해줘요.
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        return authentication;

    }

    @Override  //JWT 토큰 만들어서 응답을 합시다.
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


       PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

       //JWT는 Header와 payload, Sifnature 이렇게 세 부분으로 이루어짐


       String jwtToken = JWT.create()
               .withSubject(principalDetails.getUsername())
               .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRE_TIME)) //토큰의 유효기간 현재시간으로부터 1시간
               .withClaim("id", principalDetails.getUser().getId()) //인증에 필요한 정보
               .withClaim("username", principalDetails.getUser().getPassword())
               .sign(Algorithm.HMAC256(JwtProperties.SECRET));

        String jwtRefreshToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_VALIDATION_TIME))
                .withClaim("id", principalDetails.getUser().getId()) //인증에 필요한 정보
                .withClaim("username", principalDetails.getUser().getPassword())
                .sign(Algorithm.HMAC256(JwtProperties.SECRET));

        Cookie refreshCookie = cookieUtill.createCookie(JwtProperties.REFRESH_TOKEN_NAME, jwtRefreshToken);
        response.addCookie(refreshCookie);

        response.addHeader(JwtProperties.TOKEN_HAEDER, JwtProperties.TOKEN_PRIFIX + jwtToken);


        CMRespDto cmRespDto = new CMRespDto(1, principalDetails.getUser());
        Script.responseData(response, cmRespDto);
    }
}
