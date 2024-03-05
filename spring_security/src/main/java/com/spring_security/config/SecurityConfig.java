package com.spring_security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//버전으로 인한 주의 할점
//spring의 예전 버전에선는
// WebSecurityConfigurerAdapter을 상속 받아서 사용했었지만 현재는 상속 받아서 사용하는것이 아닌
//아래와 같이 @Bean을 통해서 filterChain을 설정해서 사용한다.

@Configuration
@EnableWebSecurity // -> 스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {



    //헤당 메서드의 리턴되는 오브젝트를 IOC로 등록해줌
    //@Bean 어노테이션을 사용하면 다른 파일에서도 사용이 가능해 진다.
    @Bean
    public BCryptPasswordEncoder encoderPwd(){
        return new BCryptPasswordEncoder();
    }
    //hasAnyRole에는 ROLE_ 이포함되면안되지만 User Entity에는 포함되어야한다.(ROLE_USER)
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login가 호출되면 시큐리티가 낚아채서 대신 로그인 진행
                .defaultSuccessUrl("/") //성공하면 여기로 이동
                .and()
                .httpBasic().disable()
//                .apply(new MyCustomDsl()) // 커스텀 필터 등록
//                .and()
                .authorizeRequests()
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/manager/**").hasAnyRole("MANAGER" , "ADMIN")
                .requestMatchers("/admin/**").hasAnyRole( "ADMIN")
                .anyRequest().permitAll();

                return http.build();

    }


}

