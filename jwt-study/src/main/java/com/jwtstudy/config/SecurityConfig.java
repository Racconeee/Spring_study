package com.jwtstudy.config;

import com.jwtstudy.filter.MyFilter1;
import com.jwtstudy.filter.MyFilter2;
import com.jwtstudy.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
        sharedObject.userDetailsService(this.userDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();

        http.authenticationManager(authenticationManager);

        http
//                .addFilterBefore(new MyFilter2() , BasicAuthenticationFilter.class); 기본적으로 필터체인보다 시큐리티필터체인이 먼저 사용된다.
                .addFilter(new JwtAuthenticationFilter(authenticationManager))
                .csrf().disable().cors().disable()
                .httpBasic(basic -> basic
                        .disable())
                .sessionManagement(sessoin -> sessoin
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/user/**").authenticated()
                        .requestMatchers("/api/v1/manager/**").hasAnyRole("MANAGER" , "ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasAnyRole( "ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin().disable();

        return http.build();

    }

}
