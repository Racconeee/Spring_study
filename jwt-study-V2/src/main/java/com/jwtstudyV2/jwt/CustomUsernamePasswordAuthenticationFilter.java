package com.jwtstudyV2.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtstudyV2.global.exception.CustomException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    //Security 기본 경로가 /login 인데 변경 안하고 그대로 사용함
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login"; // "/login"으로 오는 요청을 처리
    private static final String HTTP_METHOD = "POST"; // 로그인 HTTP 메소드는 POST
    private static final String CONTENT_TYPE = "application/json"; // JSON 타입의 데이터로 오는 로그인 요청만 처리
    private static final String USERNAME_KEY = "username"; // 회원 로그인 시 이메일 요청 JSON Key : "username"
    private static final String PASSWORD_KEY = "password"; // 회원 로그인 시 비밀번호 요청 JSon Key : "password"
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); // "/login" + POST로 온 요청에 매칭된다.
    //왜 사용하는지 기억이 나지않는다면 Login Form 인증 로직 플로우를 찾아보자
    //생성자에서는 필터가 처리할 요청을 지정하는데 사용되는 AntPathRequestMatcher 객체를 초기화합니다.



    private final ObjectMapper objectMapper;
    public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); // 위에서 설정한 "login" + POST로 온 요청을 처리하기 위해 설정
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)){
            throw new CustomException("요청은 "+CONTENT_TYPE+ "형식으로 요청해야 합니다. 현재 타입은 "+request.getContentType() , HttpStatus.BAD_REQUEST);
        }


        String messageBody = StreamUtils.copyToString(request.getInputStream() , StandardCharsets.UTF_8);
        log.info("messageBody : {}" , messageBody);

        Map<String , String> usernamePasswordMap = objectMapper.readValue(messageBody , Map.class);
        log.info("usernamePasswordMap : {}" ,usernamePasswordMap);
        String username = usernamePasswordMap.get(USERNAME_KEY);
        String password = usernamePasswordMap.get(PASSWORD_KEY);

        UsernamePasswordAuthenticationToken Token = new UsernamePasswordAuthenticationToken(username,password);

        Authentication authentication = getAuthenticationManager().authenticate(Token);
        return authentication;


    }


}
