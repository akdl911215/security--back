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

        return "Admin 정보";
    }

}
