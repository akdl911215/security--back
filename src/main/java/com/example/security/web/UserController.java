package com.example.security.web;


import com.example.security.domain.User;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/find")
    public User findByUsername() {

        log.info("유저정보 찾기");

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

}
