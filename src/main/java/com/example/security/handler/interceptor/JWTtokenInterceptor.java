//package com.example.security.handler.interceptor;
//
//import com.example.security.config.auth.PrincipalDetails;
//import com.example.security.config.jwt.JwtProperties;
//import com.example.security.handler.customexception.SessionNotFoundException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//@Slf4j
//public class JWTtokenInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//                log.info("my 인터셉터");
//
////                String token = request.getHeader(JwtProperties.TOKEN_HAEDER)
////
////                //jwt
////
////                //세션 <-> JWt의 장점 이란 게
////                //1. 언어에 독립적임. json은 모든 언어들간의 표준통신언어,
////                //2. 확장성이 유리해요.
////
////                if (principal == null){
////                    //세션이 없으면 통과를 못 시켜주도록
////                    log.info("인터셉터 발동!");
////
////                    throw new SessionNotFoundException("권한이 없습니다!");
////                }else{
////                    return true;
////                }
//    }
//
//    //PostHandle() – 컨트롤러 실행 후 ViewResolver Or MessageConverter가 관여하기 전 실행됨.
//    //afterComplete() – view가 정상적으로 렌더링 된 후 제일 마지막 실행됨.
//
//
//
//}

//멍청한 시큐리티가 1타 강사란 말이지네욧. 현우진 공짜인데,