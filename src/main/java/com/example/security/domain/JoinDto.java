package com.example.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JoinDto {

    private String username;
    private String password;


    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .role(Role.USER)
                .build();

    }


}
