package com.example.security.service;


import com.example.security.domain.JoinDto;
import com.example.security.domain.User;
import com.example.security.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;


    public void join(JoinDto joinDto){

        User user = joinDto.toEntity();

        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        user.setPassword(encPassword);


        //jpa는 기본 api는 엔티티 클래스만 받도록 되어있어요.
        userRepository.save(user);
    }



    public User findbyUsername(String name){

        return userRepository.findByUsername(name);

    }



}
