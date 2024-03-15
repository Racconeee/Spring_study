package com.jwtstudyV2.global;

import com.jwtstudyV2.global.exception.CustomException;
import com.jwtstudyV2.model.User;
import com.jwtstudyV2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userRepository.findByUsername(username).orElseThrow(() -> new CustomException("입력하신 ID는 존재하지 않습니다.", HttpStatus.BAD_REQUEST));
        log.info("생성된 userEntity : {}" , userEntity);

        return new PrincipalDetails(userEntity);
    }
}
