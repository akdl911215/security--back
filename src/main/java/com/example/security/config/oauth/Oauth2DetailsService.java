package com.example.security.config.oauth;


import com.example.security.config.auth.PrincipalDetails;
import com.example.security.domain.Role;
import com.example.security.domain.User;
import com.example.security.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class Oauth2DetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("Oath2.0 로그인 진행");
        log.info(userRequest.getAccessToken().getTokenValue());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("잘 받아왓는지: " + oAuth2User.getAttributes());

        //저 왔어요....

        return proessOauthUser(userRequest, oAuth2User);
    }


    //구글 로그인 프로세스
    private OAuth2User proessOauthUser(OAuth2UserRequest userRequest, OAuth2User oAuth2User){

        OAuth2UserInfo oAuth2UserInfo = null;
        log.info("어떤 경로로 로그인 되었는지 check " + userRequest.getClientRegistration().getClientName());

        if (userRequest.getClientRegistration().getClientName().equals("Google")){
            oAuth2UserInfo = new GoogleInfo(oAuth2User.getAttributes());
        }

        //저희 DB에는 그 사용자에 대한 정보가 없을 거잖아요.
        User userEntity = userRepository.findByUsername(oAuth2UserInfo.getUsername());

        UUID uuid = UUID.randomUUID();
        String encPassword = encoder.encode(uuid.toString());

        if(userEntity == null){
            log.info("얘네는 소셜로그인으로 최초 로그인한 사용자, 자동으로 우리 db로 회원가입을 진행시키자");

            User user = User.builder()
                    .username(oAuth2UserInfo.getUsername())
                    .password(encPassword)
                    .role(Role.USER)
                    .build();

            userEntity = userRepository.save(user);

            return new PrincipalDetails(userEntity, oAuth2UserInfo.getAttributes());

        }else{

            log.info("이미 회원정보가 있습니다. 바로 로그인합니다.");

            return new PrincipalDetails(userEntity, oAuth2UserInfo.getAttributes());

        }


    }





    //Oauth2.0 개념에 대해서 잠깐 설명할게요.
    //Open auth. 인증을 오픈했다는 건데, 권한위임시키는거에요. 서버에서  OAuth을 구현하는 데 크게 2가지 방식이 있어요.
    //1. code 방식이 있고, credential 방식
    //클라이언트가 있을거잖아요. 프론트서버, 프론트서버에서 저희 서버로 나 로그인할게 라고 요청이 와요.
    //이때 제 서버가 응답을 해줘요. 근데 무슨 응답? 구글 로그인 창을 건네줘요. 이 창의 주인은 구글이죠
    //구글 Oauth2.0 리소스 서버가 있어요. 여기로 리다이렉트 시키는 거에요. 그럼 얘네가 저희보다는 잘 만들었을 거잖아요.
    //그럼 구글 그 자기의 그 리소스 서버의 DB와 막 검증해서, 통과가 되면.. 저희 서버에게 code를 하나 줘요.
    //제 서버는 code를 받는 즉시 클라이언트 대신에 구글에 접근할 수 있는 권한을 가지게 되요.(권한위임)


    //저희 서버는 이제 이 클라이언트가 인증된 사람이라는 걸 알 수가 있죠. 근데 이 상태로 세션에 등록하면 인증은 됐지만, 권한을 몰라
    //얘가 인증은 됐는데 누군지를 몰라, 여기서 (코드는 영원하기 때문에 이걸 인증수단으로 사용하면 위험해요.) 제 서버는 나 코드 받았
    //으니까 구글에게 토큰 하나 달라고 요청해요. 구글은 JWT 토큰을 발행해주는데, 이걸 access token이라고 해요. 그럼 제 서버는 이
    //access token을 이용해서 유저정보를 꺼내오면 되요. 이 꺼내온 정보들을 자기 DB에 저장시켜도 되고,
    //여까지가 code 방식


    //credintal 방식

    //만약에 통신을 하잖아요. 클라이언트 입장이 안드로이드나, 리액트 같은 거다. 그럼 code 방식이 안 됨
    //일반적인 방식에서는 제 서버가 클라이언트로부터 오는 요청을 구글 api 페이지 띄워서 연결시켜주는데, 안드로이드에서는 새 창이라는
    //개념이 없죠. 리액트는 spa a link를 못 써요. 내 서버랑 클라이언트 서버 사이에서 구글 창이 끼어들 여지가 없어요. 리소스 오너,(클라이언트)
    //가 먼저 구글측으로 로그인을 하고, 그런 다음 구글이 리소스오너에게 리소스 오너에 대한 정보와 accesstoken을 주면,
    //그런 다음다시 클라이언트(리소스 오너)가 제 서버로 그걸 주는 거에요.




    //jwt 프론트서버에서 어떻게 처리할지는, localstorage 보관한다. sessionstrooage, 쿠키로 감싸서 보관한다. 각각의 장단점이 있어요.
    //일단 저는 localstroage, 그러면 우리가 이런것도 자동 로그인 클라이언트 사이드에서 토큰은 유효기간이 ㅜ
    //localstroage jwt 를 저장을 해요. localstroage 그 브라우저를 전부 다 끄고 재시작하거나, 아니면 서버 재부팅이 아니면 계속 남아있거드뇽.
    //예를 들어 jWT 유효기간을 10일이라고 잡고, 사용자는 절전 모드로 하고, 브라우저 창 하나를 끄고 다시 저희 페이지로들어와요.
    //남아있겠죠. 토큰이 토큰을 바로 랜더링되는 시점에서 서버로 던지면 자동으로 로그인 처리가 되겠죠.
    // .JWT 토큰의 위험성.. localstroage 저장하는 게 어떤 점이 위험하냐면  나머지 사용자들이 다 볼 수 있어요.

    //보안이 다 뜷리는 거에요. 쿠키, 서버에서 jwt 를 쿠키로 감싸서 주는 방법이 있어요. 장점이  한 번 쿠리를
    //그래서 보통 accesstoken // 10분 refreshtoken//일주일
    //그래서 이걸 보통은 redis, 인 메모리 기반의 키 value로 이루어진 캐싱 기능을 지원해주는 nosql이에요.
    //redis 기간을 기간이 지나면 자동으로 refres 사용자가 로그인할때, redis db에 사용자가 accesstoken 기한이 recdis refresgtione
    //accesstoken 사용자한테 그런 식으로 하는 방법이 있고.. 저희는 그냥 단순히 jwt 하나만 가지고 통신하는 거로 테스트해볼게요.







}
