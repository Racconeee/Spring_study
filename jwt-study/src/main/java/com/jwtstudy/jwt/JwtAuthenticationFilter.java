package com.jwtstudy.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtstudy.auth.PrincipalDetails;
import com.jwtstudy.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;

//Spring 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
//Login 요청해서 username , password 전송하면 (Post)
//usernamePasswordAuthenticationFilter 동작을 함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;


    //login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication result = null;
        System.out.println("로그인 시도 : jwtFilter");
        try {

//            BufferedReader br = request.getReader();
//            String input = null;
//            while((input = br.readLine()) != null){
//                System.out.println(input);
//            }
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println("++++++++++++++++++++");

            System.out.println(user);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());


            //PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨
            //username 만 우리가 password는 스프링 내부에서 알아서 해줌
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@");

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            System.out.println(principalDetails.getUser().getUsername());
            System.out.println(authentication);
            result = authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result == null) {
            System.out.println("===============================");

            //1.username , password 받아서
            //2.정상인지 로그인 시도를 해보는거예요. authentiactionManager로 로그인 시도를 하면 PrincipalDetailsService가 호출
            //그러면 loadByusername()함수가 싪행된다.

            //3.PrincipalDetails를 세션에 담고 (안담으면 권한 문제에 걸림 권한관리르 위해서 담아야함 )

            //4.JWT토큰을 만들어서 응답해주면 됨.
        }
        return result;
    }
}
