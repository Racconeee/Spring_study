package com.jwtstudyV2.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtstudyV2.global.PrincipalDetails;
import com.jwtstudyV2.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

//현재 프로젝트에서는 사용은 안되지만 AbstractAuthenticationProcessingFilter와 비슷한 기능
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername() , user.getPassword());

            //이 부분에서 PrincipalDetailsService,PrincipalDetails실행된다.
            //authenticationManager를 이용해서 로그인 시도
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
