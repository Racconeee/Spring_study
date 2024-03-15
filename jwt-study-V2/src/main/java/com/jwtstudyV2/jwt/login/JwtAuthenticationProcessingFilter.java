package com.jwtstudyV2.jwt.login;

import com.jwtstudyV2.global.exception.CustomException;
import com.jwtstudyV2.jwt.JwtService;
import com.jwtstudyV2.model.User;
import com.jwtstudyV2.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {


    private static final String NO_CHECK_URL = "/login";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public JwtAuthenticationProcessingFilter(JwtService jwtService, UserRepository userRepository ,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("request : {}" , request);
        if(request.getRequestURL().equals(NO_CHECK_URL)){
            filterChain.doFilter(request ,response);
            return;
        }

        String refreshToken = jwtService.extractRefreshToken(request) // refreshToken 에서 Bearer 제거
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if(refreshToken != null){
            checkRefreshTokenAndReIssueAccessToken(response , refreshToken);
            return;
        }
        if(refreshToken == null){
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }



    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        User userEntity = userRepository.findByRefreshToken(refreshToken).orElseThrow( () -> new CustomException("checkRefreshTokenAndReIssueAccessToken : 회원이없습니다." , HttpStatus.BAD_REQUEST));
        String reIssuedRefreshToken = reIssueRefreshToken(userEntity);
        jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(userEntity.getUsername()), reIssuedRefreshToken);
    }

    /**
     * [리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드]
     * jwtService.createRefreshToken()으로 리프레시 토큰 재발급 후
     * DB에 재발급한 리프레시 토큰 업데이트 후 Flush
     */
    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }

    /**
     * [액세스 토큰 체크 & 인증 처리 메소드]
     * request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
     * 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
     * 그 유저 객체를 saveAuthentication()으로 인증 처리하여
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
     * 그 후 다음 인증 필터로 진행
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {

        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractUsername(accessToken)
                        .ifPresent(username -> userRepository.findByUsername(username)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(User user) {
        log.info("");
        System.out.println("@@");
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .roles(user.getRoles().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}