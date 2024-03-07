package com.jwtstudy.auth;

import com.jwtstudy.model.User;
import com.jwtstudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {


    private final UserRepository userRepository;
    //PrincipalDetails 내용 기반으로 사용
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 User이름()");
        User userEntity = userRepository.findByUsername(username);
        System.out.println("userEntity" + userEntity);
        return new PrincipalDetails(userEntity);
    }
}
