package com.spring_security.config.auth;

import com.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.spring_security.model.User;


//단 loadUserByUsername 파라미터인 username 과 프론트에서 /login으로 요청이 오는 파라미터 이름이 같아야한다
// 다르다면 SecurityConfig에서 프론트의 파라미터와 이름을 맞춰 줘야 한다.
//시큐리티 session = Authentication = UserDetails
//시큐리티 session = Authentication(내부 UserDetails) = UserDetails
//시큐리티 session(내부 Authentication(내부 UserDetails) 로 변경된다
//principalDetails -> UserDetails type

//시큐리티 설정에서 loginProcessingUrl("login");
//login 요청이 오면 자동으로 UserDetailsService타입으로 IoC 되어 있는 loadUserByUsername함수가 실행

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //알다시피 findBy규칙 Username 문법
        // select * from user where username = ? 쿼리가 실행된다. -> jpa 에서 알아서 메소드 이름 기준으로 생성해줌
        User userEntity = userRepository.findByUsername(username);
        // 이 부분에서 유저 정보에 대한 값 세팅 로직을 작성
        if (userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
