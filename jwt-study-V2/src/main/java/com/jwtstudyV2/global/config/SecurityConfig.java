package com.jwtstudyV2.global.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtstudyV2.global.PrincipalDetailsService;
import com.jwtstudyV2.jwt.CustomAuthenticationFilter;
import com.jwtstudyV2.jwt.CustomUsernamePasswordAuthenticationFilter;
import com.jwtstudyV2.jwt.Handler.LoginFailureHandler;
import com.jwtstudyV2.jwt.Handler.LoginSuccessHandler;
import com.jwtstudyV2.jwt.JwtService;
import com.jwtstudyV2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalDetailsService principalDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{




        http
//                .addFilterBefore(new MyFilter2() , BasicAuthenticationFilter.class); 기본적으로 필터체인보다 시큐리티필터체인이 먼저 사용된다.
                .csrf().disable().
                cors().disable()
                .addFilter(new CustomAuthenticationFilter(authenticationManager())) // CustomAuthenticationFilter을 필터로 등록을 해야 로그인의 기본 경로인 /login이 생성된다.
                .httpBasic(basic -> basic
                        .disable())
                .sessionManagement(sessoin -> sessoin
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/user/**").hasAnyRole( "USER","MANAGER" , "ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin().disable();
                http.addFilterAfter(customUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
//                .addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //사용자 정보 기반으로 JWT 토큰을 만들기에 jwtService , userRepository를 의존성 주입을 해주어야 한다.
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository); // 수정: 의존성 주입
    }
   @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(); // 수정: 의존성 주입
    }

    @Bean
    CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter
                = new CustomUsernamePasswordAuthenticationFilter(objectMapper);

        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(principalDetailsService);
        return new ProviderManager(provider);
    }


}


