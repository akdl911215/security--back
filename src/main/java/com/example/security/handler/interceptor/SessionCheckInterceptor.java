package com.example.security.handler.interceptor;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.handler.customexception.SessionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class SessionCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

                log.info("my 인터셉터");

                HttpSession session = request.getSession();

                PrincipalDetails principal = (PrincipalDetails) session.getAttribute("principal");

                if (principal == null){
                    //세션이 없으면 통과를 못 시켜주도록
                    log.info("인터셉터 발동!");

                    throw new SessionNotFoundException("권한이 없습니다!");
                }else{
                    return true;
                }
    }

    //PostHandle() – 컨트롤러 실행 후 ViewResolver Or MessageConverter가 관여하기 전 실행됨.
    //afterComplete() – view가 정상적으로 렌더링 된 후 제일 마지막 실행됨.

}
