package com.jwtstudy.controller;


import com.jwtstudy.model.User;
import com.jwtstudy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.http.HttpResponse;
import java.nio.charset.Charset;

@ToString
@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;



    @GetMapping("/home")
    public String home(Model model) {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null)
            ip = req.getRemoteAddr();

        model.addAttribute("clientIP", ip);
        System.out.println("ip"+ip);
        return "home";
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
        return "admin";
    }
}
