package com.jwtstudyV2.controller;

import com.jwtstudyV2.global.exception.CustomException;
import com.jwtstudyV2.global.exception.MemberErrorCode;
import com.jwtstudyV2.global.exception.MemberException;
import com.jwtstudyV2.jwt.JwtService;
import com.jwtstudyV2.model.Role;
import com.jwtstudyV2.model.User;
import com.jwtstudyV2.model.UserDto;
import com.jwtstudyV2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/")
    public ResponseEntity<String> home(){


        return ResponseEntity.ok("home");
    }
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserDto dto){

        System.out.println(dto.getUsername());
        User userEntity = User.builder()
                .username(dto.getUsername())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .roles(Role.USER)
                .build();
        userRepository.save(userEntity);

        return ResponseEntity.ok().body("home");
    }

    @PostMapping("/admin/**")
    public ResponseEntity<String> amdin(){
        return ResponseEntity.ok("admin_home()");
    }
    @PostMapping("/play/**")
    public ResponseEntity<String> play(HttpServletRequest request){
        String optional = jwtService.extractAccessToken(request).orElseThrow( () -> new CustomException("유저 정보가 없어요" , HttpStatus.BAD_REQUEST));

        System.out.println(optional);
        String optional1 = jwtService.extractUsername(optional).orElseThrow( () -> new CustomException("유저 정보가 없어요" , HttpStatus.BAD_REQUEST));
        System.out.println(optional1);

        return ResponseEntity.ok("admin_home()");
    }


    @GetMapping("/ex/customException")
    public ResponseEntity OK_customException() {
        throw new CustomException("customException",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/ex/MemberException")
    public ResponseEntity MemberException() {
        throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_ERROR);
    }
}
