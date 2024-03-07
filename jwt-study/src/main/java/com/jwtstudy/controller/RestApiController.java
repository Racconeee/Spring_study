package com.jwtstudy.controller;


import com.jwtstudy.model.User;
import com.jwtstudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@ToString
@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    @GetMapping("/home")
    public String home(){
        return "<h1>home</h1>";
    }
    @GetMapping("/token")
    public String token(){
        return "<h1>token</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody User user){
        System.out.println(user.toString());
        System.out.println("오잉?");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);

        return "회원가입완료";

    }
}
