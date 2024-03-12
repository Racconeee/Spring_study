package com.jwtstudyV1.controller;


import com.jwtstudyV1.model.Role;
import com.jwtstudyV1.model.User;
import com.jwtstudyV1.model.UserDto;
import com.jwtstudyV1.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
public class LoginController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/signup")
    public ResponseEntity<User> login(@Valid @RequestBody UserDto dto ){

        System.out.println("회원가입 진행");
        User user = User.builder()
                .username(dto.getUsername())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build();


        user.setRoles(Role.ADMIN);
        userRepository.save(user);

        return ResponseEntity.ok().body(userRepository.findById(user.getId()).orElseThrow());
    }


    @GetMapping("/home")
    public String home(HttpServletRequest request){
        System.out.println(request.getHeader("Authorization"));
        return "집이당";
    }



    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }
    @GetMapping("/api/v1/admin")
    public String admin(){
        System.out.println("성공");
        System.out.println("성공");
        System.out.println("성공");
        System.out.println("성공");
        System.out.println("성공");
        System.out.println("성공");
        System.out.println("성공");
        System.out.println("성공");
        System.out.println("성공");
        System.out.println("성공");
        System.out.println("성공");
        return "admin";
    }
}
