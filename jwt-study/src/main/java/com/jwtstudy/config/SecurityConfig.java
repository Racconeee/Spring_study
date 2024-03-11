package com.jwtstudy.config;

import com.jwtstudy.jwt.JwtAuthenticationFilter;
import com.jwtstudy.jwt.JwtAuthorizationFilter;
import com.jwtstudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
        sharedObject.userDetailsService(this.userDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();

        http.authenticationManager(authenticationManager);

        http
//                .addFilterBefore(new MyFilter2() , BasicAuthenticationFilter.class); 기본적으로 필터체인보다 시큐리티필터체인이 먼저 사용된다.
                .addFilter(new JwtAuthenticationFilter(authenticationManager))
                .addFilter(new JwtAuthorizationFilter(authenticationManager ,  userRepository))
                .csrf().disable().
                cors().disable()
                .httpBasic(basic -> basic
                        .disable())
                .sessionManagement(sessoin -> sessoin
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/user/**").hasAnyRole( "USER","MANAGER" , "ADMIN")
                        .requestMatchers("/api/v1/manager/**").hasAnyRole("MANAGER" , "ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasAnyRole( "ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin().disable();

        return http.build();

    }

}
