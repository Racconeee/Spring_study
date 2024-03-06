package com.spring_security.config.oauth;

import com.spring_security.config.auth.PrincipalDetails;
import com.spring_security.model.User;
import com.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

//구글로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴 (OAuth-Client 라이브러리) -> AccessToken 요청
//userRequest 정보 -> loadUser함수 호출 -> 회원 프로필

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    //google로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);


        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oauth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String email = oauth2User.getAttribute("email");
        BCryptPasswordEncoder bCryptPasswordEncoder;

        Optional<User> userOptional = userRepository.findByProviderAndProviderId(provider , providerId);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.updateEmail(email);
        }else {
            user = User.builder()
                    .username(username)
                    .email(email)
                    .role("ROLE_USER")
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(user);
        }
        return new PrincipalDetails(user , oauth2User.getAttributes());
    }
}
