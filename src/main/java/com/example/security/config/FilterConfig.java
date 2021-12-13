package com.example.security.config;

import com.example.security.handler.filter.MyAuthencationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {


    @Bean
    public FilterRegistrationBean<MyAuthencationFilter> myAuthencationFilterFilterRegistrationBean(){

        FilterRegistrationBean<MyAuthencationFilter> bean =
                new FilterRegistrationBean<>(new MyAuthencationFilter());

        bean.addUrlPatterns("/find");
        bean.addUrlPatterns("/check");

        //서블릿 필터는 스프링 진입 전에 돌아요. 톰캣과 스프링 사이에 서블릿필터가 있잖아요.\
        //타이밍 문제 때문에 익셉션을 못 탐.. 스프링 안에 들어와야지 되는데

        //필터의 단점이 전처리만 되지, 후처리가 안 된다는 거에요.
        //이런 문제점을 해결하려면 인터셉터가 필요해요.
        return bean;
    }


}
