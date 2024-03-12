package com.jwtstudyV1.auth;

import com.jwtstudyV1.model.User;
import com.jwtstudyV1.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



//로그인을 하면 loadUserByUsername() 호출된다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //로그인을 하는데 값이 없다면 예외 터트리고 PrincipalDetails 객체를 반환
        User user = userRepository.findByUsername(username).orElseThrow( () ->
                new UsernameNotFoundException("not found loginId : " + username));

        System.out.println("PrincipalDetailsService 실행 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ" );
        System.out.println("현재 들어온 User");
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getRoles());

        return new PrincipalDetails(user);


    }
}
