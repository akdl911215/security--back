package com.example.security.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {

    @GetMapping("/get")
    public String admin(){
        //관리자 권한이 필요한 요청
        
        return "Admin 정보";
    }

}
