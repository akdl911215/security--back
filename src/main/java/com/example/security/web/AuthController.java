package com.example.security.web;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.config.auth.PrincipalDetails;
import com.example.security.config.jwt.JwtProperties;
import com.example.security.config.oauth.GoogleInfo;
import com.example.security.config.oauth.OAuth2UserInfo;
import com.example.security.domain.*;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @GetMapping("/check")
    public String check(){

        log.info("시큐리티가 통과를 시켜주는 지 안하는지 봐주세요");

        return "순서";
    }


    @PostMapping("/join")
    public String join(@RequestBody JoinDto joinDto){

        log.info("회원가입 =>{} ", joinDto);
        userService.join(joinDto);
        return  null;

    }


    //시큐리티가
    // 1.인증 2.권한 3.http 프로토콜 사용하는 보안 정책, csrf, cors, 4.oauth2.0
    // 등등을 처리해주는 내부적으로 필터와 인터셉터로 이루어지는
    //외부 라이브러리라고 했죠.



    @PostMapping("/socialLogin")
    public CMRespDto socialLogin(@RequestBody Map<String, Object> data, HttpServletResponse response){

        log.info("소셜 로그인 진행 " + data);
        OAuth2UserInfo googleUser =
                new GoogleInfo((Map<String, Object>) data.get("profileObj"));

        log.info("googleUser: " + googleUser);
        User userEntity = userRepository.findByUsername(googleUser.getId());
        UUID uuid = UUID.randomUUID();
        String encPassword = encoder.encode(uuid.toString());
        if(userEntity == null){
            log.info("얘네는 소셜로그인으로 최초 로그인한 사용자, 자동으로 우리 db로 회원가입을 진행시키자");

            User user = User.builder()
                    .username(googleUser.getId())
                    .password(encPassword)
                    .role(Role.USER)
                    .build();

            userEntity = userRepository.save(user);

        }

        String jwtToken = JWT.create()
                .withSubject(userEntity.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRE_TIME)) //토큰의 유효기간 현재시간으로부터 1시간
                .withClaim("id", userEntity.getId()) //인증에 필요한 정보
                .withClaim("username", userEntity.getPassword())
                .sign(Algorithm.HMAC256(JwtProperties.SECRET));

        response.addHeader(JwtProperties.TOKEN_HAEDER, JwtProperties.TOKEN_PRIFIX + jwtToken);

        //소셜 로그인
        //아무튼 간에 여기서 토큰을 만들고
        //return new CMRespDto(1, )
        return  new CMRespDto(1, userEntity);
    }


//    @GetMapping("/logout")
//    public void logout(){
//
//
//
//    }


    @GetMapping("/loadUser")
    public CMRespDto<?> loadUser(@AuthenticationPrincipal PrincipalDetails principal, HttpServletResponse resp) throws IOException {

        User user = principal.getUser();
        if (user == null) {
            log.info("user가 null이면");
            resp.sendRedirect("/logout");
        }

        log.info("유저정보 유지" + user);

        return new CMRespDto<>(HttpStatus.OK.value(), user);
    }



}
