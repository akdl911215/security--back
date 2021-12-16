package com.example.security.config;

import com.example.security.config.jwt.*;
import com.example.security.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@EnableWebSecurity //시큐리티 활성화
@Configuration // ioc 등록
public class SercurityConfig extends WebSecurityConfigurerAdapter {


    private final UserRepository userRepository;
    private final HttpSession session;

    @Bean//시큐리티가 비밀번호 검증할 때, 무조건 암호화해서 회원가입을 시켜줘야됨.
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//        web.ignoring().mvcMatchers("/**"); //문지기가 사람들을 다 통과시켜줌
//    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //내서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정
        config.addAllowedOriginPattern("*"); //모든 ip에 응답을 허용하겠다.
        config.addAllowedMethod("*"); // 모든 post,get,put,delete, patch ..요청을 허용하겠다.
        config.addAllowedHeader("*"); //모든 header에 응답을 허용하겠다.
        //config.addAllowedOrigin("*");
        config.addExposedHeader(JwtProperties.TOKEN_HAEDER);

        //기본적으로 웹이 같은 도메인이 아니면 요청을 허용시켜주지 않아여.
        //프론트엔드 서버가 도메인 주소를 naver.com  벡엔드서버가 daum.net 같은 ip라도 port가 다르면

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //statefull stateless 방식의 차이.. 저희가 자바스크립트 사이드,
        //jsp 로 시큐리티를 jsession을 자바스크립트,
                .and()

                .addFilter(jwtLoginFilter())
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userRepository, session))

                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAceessDeniedHandler())
                .and()
                .authorizeRequests()
                .antMatchers("/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .logout()
                .logoutUrl("/mylogout")
                .logoutSuccessHandler(new JwtLogoutSuccessHandler())
                .and()
                .oauth2Login()
                .userInfoEndpoint()

                ;
                //.antMatchers("/user").access("hasRole("ROLE_USER")")
                //권한이 다르잖아요.
                //.logout();


        //jwt를 활용한 인증과 권한 처리를 저희가 직접 한 거에요. 시큐리티를 통해서.
        // 내일 소셜 로그인, Open auth 인증을 열어, refreshToken과 accessToken 개념 설명.
        // 1. 무식하게 컨트롤러에서 직접 처리한다. 2. 조금 덜 무식하게 내가 직접 시큐리티 같은 필터와 인터셉터를 만든다.
        //3. 시큐리티를 커스텀해서 사용한다. 4. 구글이나, 페이스북 같은 경우에 auth 인증 서비스, 시큐리티, 유효한
        //내일 시간이 될 리액트에서 간단한 login app, 시큐리티 연동하는 거 해볼거고, 소셜로그인,
        //spa 에서 구현하는 거랑 그냥 멀티페이지에서 구현하는게 다르거든요. 두 개가 소셜로그인 클라이언트 사이트 구현하는 방법이 하나 있고
        //백엔드 서버에서 구현하는 방법이 있거든요. 프론트에서 구현하는 게 더 간편해요.
        //그거의 차이점을 알아볼거고..


//        http.csrf().disable()//csrf는 form 태그를 통한 공격인데 시큐리티가 이걸 방지하기 위해
//                .headers().frameOptions().disable()
//        ;
//        //form 태그로 값을 전송할 떼, 어떤 value 같이 전송하도록 설정함. 자세한 내용은
//
//        //네거티브 방식 = 성문을 다 닫는거에요.  성문을 기본적으로 어떤 요청만 성문을 막아놓을거에요.
//
//        http.authorizeRequests()
//                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll() //그 외의 모든 요청은 성문을 열어놓겠다.
//                .and()
//                .formLogin()
//
//        ; //시큐리티 기본전략이 form..


        //저희가 jwt를 사용해서 로그인 처리를 해볼게요.

    }





    @Bean
    public JwtLoginFilter jwtLoginFilter() throws Exception {

        final JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(authenticationManager());
        jwtLoginFilter.setFilterProcessesUrl("/mylogin");
        jwtLoginFilter.setAuthenticationManager(authenticationManager());

        jwtLoginFilter.setAuthenticationSuccessHandler(new JwtLoginSuccessHandler());
        jwtLoginFilter.setAuthenticationFailureHandler(new JwtLoginFailureHandler());

        return jwtLoginFilter;
    }


}



//스프링부트 어플리케이션을 만들었잖아요.
//하나의 성. 성문 문지기가 있을
//시큐리티 필터가 정문 가장 앞의 문지기, 필터를
//클라이언트 rest 요청 -> 성   문지기 검사하고 성문 3개가 있다고 생각. 제일 바깥에 있는 문이 시큐리티. 그 다음에 내부적인 필터를
