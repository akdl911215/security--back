package com.example.security.handler.filter;

import com.example.security.handler.customexception.SessionNotFoundException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class MyAuthencationFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

      log.info("my MyAuthencationFilter");
//        HttpServletRequest servletRequest = (HttpServletRequest) request;
//
//        String auth = servletRequest.getHeader("Authorization"); //인증 권한이 강제로 통과를 못 시켜주는 거.
//
//        if (auth == null){
//            //세션이 없으면 통과를 못 시켜주도록
//
//            throw new SessionNotFoundException("권한이 없습니다!");
//        }

        filterChain.doFilter(request, response);
    }
}
