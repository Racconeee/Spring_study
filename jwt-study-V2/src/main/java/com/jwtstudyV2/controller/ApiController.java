package com.jwtstudyV2.controller;

import com.jwtstudyV2.global.exception.CustomException;
import com.jwtstudyV2.model.User;
import com.jwtstudyV2.model.UserDto;
import com.jwtstudyV2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final UserRepository userRepository;
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
                .build();
        userRepository.save(userEntity);

        return ResponseEntity.ok().body("home");
    }
}
