package com.jwtstudy.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtstudy.auth.PrincipalDetails;
import com.jwtstudy.model.User;

import com.auth0.jwt.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.Date;


//Spring 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
//Login 요청해서 username , password 전송하면 (Post)
//usernamePasswordAuthenticationFilter 동작을 함


//1.username , password 받아서
//2.정상인지 로그인 시도를 해보는거예요. authentiactionManager로 로그인 시도를 하면 PrincipalDetailsService가 호출
//그러면 loadByusername()함수가 싪행된다.

//3.PrincipalDetails를 세션에 담고 (안담으면 권한 문제에 걸림 권한관리르 위해서 담아야함 )

//4.JWT토큰을 만들어서 응답해주면 됨.


/*
* usernamePasswordAuthenticationToken을 통해서 username, password로 token을 만들어주고
* authenticationManager에게 authenticaitonToken을 넘겨주어 Authentication 객체를 만든다.
* Authentication -> 내가 로그인한 정보가 있다.
* */
//login 요청을 하면 로그인 시도를 위해서 실행되는 함수


//authentication은 세션을 해야하는데 여기서 리턴을 해주면 세션에 저장이 된다.
//권한 관리를 security가 대신 해준다.
//JWT 토큰 자체는 세션을 만들 이우가 없지만 권한처리때문에 Session에 넣어야한다


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication result = null;
        System.out.println("로그인 시도 : jwtFilter");

        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이 리턴됨
            //DB에 있는 username 과 password가 일치하다는 뜻
            //username 만 우리가 password는 스프링 내부에서 알아서 해줌
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
// principalDetails.getUser().getUsername() 여기에 값이 있다면 정상적으로 로그인이 되었다
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            result = authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication함수가 호출된다
    //이떄 로그인 성공하면 successfulAuthentication로 오니 여기서 JWT토큰을 response 해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
            .withSubject("cos토큰")
            .withExpiresAt(new Date(System.currentTimeMillis() + (60000*10)))
            .withClaim("id" ,principalDetails.getUser().getId())
            .withClaim("username" ,principalDetails.getUser().getUsername())
            .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization" , "Bearer " + jwtToken);
    }

}
