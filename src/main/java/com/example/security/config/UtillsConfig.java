package com.example.security.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtillsConfig {


    //@Bean 싱글톤
    public ObjectMapper objectMapper(){
        return new ObjectMapper();

        //Bean으로 등록 안 시키게

        //멀티 스레드로 작업을 하면 thread safe


    }

}
