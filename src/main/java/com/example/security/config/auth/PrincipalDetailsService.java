package com.example.security.config.auth;


import com.example.security.domain.User;
import com.example.security.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Override //Authentication Maneger 가 보내줌
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("username=>{}",username);

       //AuthenticationFilter가 일로 보내줌
        User principal = userRepository.findByUsername(username);

        //신기하지 않아요? 시큐리티가 그냥 url을 제공해줘요. /login /logout
        if(principal == null){
            return null;
        }else{


            return new PrincipalDetails(principal);
        }
    }
}
