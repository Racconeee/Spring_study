package com.jwtstudyV1.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtstudyV1.auth.PrincipalDetails;
import com.jwtstudyV1.auth.PrincipalDetailsService;
import com.jwtstudyV1.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

//Uesrname ,Password 기반으로 검증
@RequiredArgsConstructor
public class JwtCustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;



    //로그인하면 이 메소드르 거침
    //결국 로그인 시도하고 Authentication 생성이 이 메서드의 목표
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtCustomAuthenticationFilter 실행 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ" );
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println("1");
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername() , user.getPassword());
            System.out.println("2");

            //이 부분에서 PrincipalDetailsService,PrincipalDetails실행된다.
            //authenticationManager를 이용해서 로그인 시도
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            System.out.println("3");

//            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("4");

            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


        System.out.println("successfulAuthentication 실행 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
            PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

            String jwtToken = JWT.create()
                    .withSubject("whykk")
                    .withExpiresAt(new Date(System.currentTimeMillis() + (60000*10)))
                    .withClaim("id" ,principalDetails.getUser().getId())
                    .withClaim("username" ,principalDetails.getUser().getUsername())
                    .sign(Algorithm.HMAC512("whykk"));


            response.addHeader("Authorization" , "Bearer " + jwtToken);

    }

}
