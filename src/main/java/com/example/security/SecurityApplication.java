package com.example.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

}



//생각보다 진도가 너무 안 나가서, 미리 다 완성해놓고 할게요.
//왜 spa 에서 하이퍼링크를 쓰면 안 되는지, 자동로그인, 쿠키로 던져줄때, 보안상 차이점, 가능하면 redis,
//어땠는지 모르겠는 리덕스 스토어로 설명.. 권한 요청 같은 거..
//웹 보안 정책 설명.. revers proxy 로 cors 우회해보자. 쿠키의 httponly 옵션과,
//월차와 렌탈 수정 후 업데이트