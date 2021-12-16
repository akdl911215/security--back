package com.example.security.web;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.security.config.auth.PrincipalDetails;
import com.example.security.config.jwt.JwtProperties;
import com.example.security.config.oauth.GoogleInfo;
import com.example.security.config.oauth.OAuth2UserInfo;
import com.example.security.domain.*;
import com.example.security.handler.customexception.SessionNotFoundException;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
public class TestController {

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

    @GetMapping("/aaaa")
    public User findByUsername(HttpServletRequest request) throws Exception {


        //log.info("check" + request.getSession());

//        HttpSession session = request.getSession();
//        PrincipalDetails principal = (PrincipalDetails) session.getAttribute("principal");
//        if (principal == null){
//            //세션이 없으면 통과를 못 시켜주도록
//            log.info("인터셉터 발동!");
//            throw new SessionNotFoundException("권한이 없습니다!");
//        }


        //근데 우리가 시큐리티를 안 써도 되요. 우리가 직접 만들 수도 있거든요.
        //어떤 권한이 필요한 요청을 만들어본다고 생각을 해보자.
        //1. 무식하게 구현하는 방법이 있어요.
        //이렇게 하면 단점이
        //1. 이렇게 권한이 필요한 요청들은 전부 다 일일히 만들어줘야 되요.
        //2. SRP의 원칙과도 어긋나요. 하나의 클래스는 하나의 책임만, 유지보수하기 쉽게 하기 위하여
        //확장성을 늘리기 위해서.



        // 라우팅, 안내원인데 문지기 역할까지 책임이 2개 얘네는 안내원인지 다른 애들이 뭘 하는지 관심도 없고, 그냥 안내만 해주는 역할
        //근데 여기서 문지기 역할까지 하면, 자기 주제를 벗어난 거에요.


        // 2. 여기서 조금 더 스마트하게 제기 직접 구현해본다면. 저희가 직접 시큐리티를 안 쓰고 문지기를 만들어볼게요.
        //참고로 핵심 비즈니스 로직이 아닌, 세션체크나 로그 남기는 것 등등을 횡단관심사 라고 함.
        //아키텍처는 관점지향(AOP), AOP를 구현할 수 있게 해주는 게, filter, 인터셉터, aop 라이브러리, @RestControllerAdvice 등등...
        //저희가 직접 이렇게 인증과 권한을 체크하는 문지기를 만들 수 있죠.
        // 필터는 하나의 성. 필터는 그 성 가장 외벽 문지기 , 인터셉터는 내부 성의 문지기(필터를 통과해서 들어온 요청들을 낚아채서 검사)
        //3. 시큐리티는 외부 1타 문지기를 초정한 거에요.
        //4. 시큐리티필터가 가장 우선순위가 높음. 문지기 중의 대빵

        return userService.findbyUsername("java");
    }



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


    
}
