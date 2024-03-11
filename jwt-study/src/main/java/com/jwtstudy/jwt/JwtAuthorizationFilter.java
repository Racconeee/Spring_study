package com.jwtstudy.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jwtstudy.auth.PrincipalDetails;
import com.jwtstudy.model.User;
import com.jwtstudy.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

//시큐리티가  filter를 가지고 있는데 그 필터중에 BasicAuthenticationFilter라는 것이 있음
//권한이나 인증이 필요한 특정 주소를 요청했을 떄 위 필터를 무조건 타게 되어있음
//만약에 권한이 인증이 필요한 주소가 아니라면 이 필터를 안타게된다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager , UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    //인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청이됨");
        String jwtHeader = request.getHeader("Authorization");
        //jwt 값이 정상적으로 들어오지않을때  -> 즉 사용자 인증이 안될떄
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request, response);
            return;
        }
        //JWT Token 을 활용해서 정상적인 유저 확인
        //Bearer ~~~~ 값으로 되어있으니 Bearer 값 제거해주자
        String jwtToken = request.getHeader("Authorization").replace("Bearer " , "");
        String username = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();
        if(username != null){
            User userEntity = userRepository.findByUsername(username);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            //username 이 Null아니기에 인증이 된 유저
            //따라서 Authentication 객체를 생성해도 된다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails , null , principalDetails.getAuthorities());
            //강제로 시큐리티 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }
}
