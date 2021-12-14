package com.example.security.handler;


import com.example.security.handler.customexception.SessionNotFoundException;
import com.example.security.handler.customexception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice //servlet에서 발생한 예외를 낚아채는
public class GlobalExceptionHandler {


    @ExceptionHandler(value = SessionNotFoundException.class)
    public String sessionNotFoundException(SessionNotFoundException e){

        log.info("여기를 타나?");
        return e.getMessage(); //
    }


    @ExceptionHandler(value = UserNotFoundException.class)
    public String userNotFoundException(UserNotFoundException e){

        return e.getMessage(); //
    }
}
