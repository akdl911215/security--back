package com.example.security.web;


import com.example.security.domain.JoinDto;
import com.example.security.domain.User;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class TestController {


    private final UserService userService;

    @GetMapping("/check")
    public String check(){

        log.info("시큐리티가 통과를 시켜주는 지 안하는지 봐주세요");

        return "zzzzz";

    }


    @PostMapping("/join")
    public String join(@RequestBody JoinDto joinDto){


        log.info("회원가입 =>{} ", joinDto);

        userService.join(joinDto);

        return  null;

    }
    

    @GetMapping("/find")
    public User findByUsername(){

        return userService.findbyUsername("java");
    }

    
}
