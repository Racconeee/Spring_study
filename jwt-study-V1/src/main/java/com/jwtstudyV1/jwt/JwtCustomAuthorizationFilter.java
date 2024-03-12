package com.jwtstudyV1.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtstudyV1.auth.PrincipalDetails;
import com.jwtstudyV1.model.User;
import com.jwtstudyV1.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtCustomAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository userRepository;

    public JwtCustomAuthorizationFilter(AuthenticationManager authenticationManager , UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {


        System.out.println("JwtCustomAuthorizationFilter class ----- 실행 ");


        String AuthenticationToken = request.getHeader("Authorization");
        System.out.println("filter -> "+ AuthenticationToken);

        if(AuthenticationToken == null || !AuthenticationToken.startsWith("Bearer")) {
            chain.doFilter(request , response);
            return;
        }
        String jwtToken = request.getHeader("Authorization").replace("Bearer " , "");
        System.out.println("@@");
        System.out.println(jwtToken);
            String username = JWT.require(Algorithm.HMAC512("whykk")).build().verify(jwtToken).getClaim("username").asString();

            log.info("username : {}", username);
            if(username != null){
                User userEntity = userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("해당 이메일의 유저가 존재하지 않습니다."));

                System.out.println(userEntity.getUsername());
                System.out.println(userEntity.getPassword());
                System.out.println(userEntity.getRoles());


                System.out.println("1");
                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                System.out.println("12");
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails , null ,principalDetails.getAuthorities());
                System.out.println("13");

                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request , response);
            }
        }

}
