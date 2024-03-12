package com.jwtstudyV2.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jwtstudyV2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value; //springframework -> 중요 Lombok X
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@Getter
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;


    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;


    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "username";
    private static final String BEARER = "Bearer ";



    //AccessToken 생성
    //Claim -> username 을 넣어서 username으로 유저 식별
    public String createAccessToken(String username){
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
                .withClaim(USERNAME_CLAIM , username)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public void sendAccessToken(HttpServletResponse response , String accessToken){
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader , accessToken);
        log.info("재발급된 AccessToken : " , accessToken);
    }

}
