package com.example.security.config;

import com.example.security.handler.customexception.SessionNotFoundException;
import com.example.security.handler.interceptor.SessionCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new HandlerInterceptor() {
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//                log.info("my 문지기");
//
//                HttpServletRequest servletRequest = (HttpServletRequest) request;
//
//                String auth = servletRequest.getHeader("Authorization"); //인증 권한이 강제로 통과를 못 시켜주는 거.
//
//                if (auth == null){
//                    //세션이 없으면 통과를 못 시켜주도록
//
//                    throw new SessionNotFoundException("권한이 없습니다!");
//                }else{
//                    return true;
//                }
//
//            }
//
//        }).addPathPatterns("/find");

        registry.addInterceptor(new SessionCheckInterceptor()).addPathPatterns("/check");

    }


//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //WebMvcConfigurer.super.addResourceHandlers(registry);
//
//        String resourcelocation = "file:C:\\Users\\songn\\Documents\\Lightshot\\";
//        String resourcelurl = "/upload/**";
//
//        registry.addResourceHandler(resourcelurl)
//                .addResourceLocations(resourcelocation);
//    }
}

