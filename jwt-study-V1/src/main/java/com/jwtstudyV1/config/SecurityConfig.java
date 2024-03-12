package com.jwtstudyV1.config;

import com.jwtstudyV1.jwt.JwtCustomAuthenticationFilter;
import com.jwtstudyV1.jwt.JwtCustomAuthorizationFilter;
import com.jwtstudyV1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                .csrf().disable().
                cors().disable()
                .addFilter(new JwtCustomAuthenticationFilter(authenticationManager))
                .addFilter(new JwtCustomAuthorizationFilter(authenticationManager , userRepository))
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

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
