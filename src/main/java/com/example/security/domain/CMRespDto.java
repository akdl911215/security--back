package com.example.security.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CMRespDto<T> {

    private Integer statusCode; //0 이면 실패, 1이면 성공
    private T data;

}
