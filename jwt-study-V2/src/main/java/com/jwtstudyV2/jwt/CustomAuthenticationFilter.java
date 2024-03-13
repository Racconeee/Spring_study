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

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("JwtCustomAuthenticationFilter 실행 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ" );
        try {
            System.out.println("1");
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername() , user.getPassword());
            System.out.println("2");

            //이 부분에서 PrincipalDetailsService,PrincipalDetails실행된다.
            //authenticationManager를 이용해서 로그인 시도
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            System.out.println("3");

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("4");

            System.out.println("JwtCustomAuthenticationFilter 실행 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡlogin성공 끝 " );
            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
