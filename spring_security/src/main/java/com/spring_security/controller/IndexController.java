package com.spring_security.controller;

import com.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.spring_security.model.User;

@Controller // view 리턴
@RequiredArgsConstructor
//@RestController // api 리턴
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    //뷰 리졸버 설정 :templates(prefix) . mustache (suffix) 생략가능
    @GetMapping({"" , "/"})
    public String index(){
        return "index";
    }


    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }
    @GetMapping("/admin")
    public @ResponseBody String admin(@ModelAttribute User user){
        System.out.println(user.getRole());
        System.out.println(user.toString());
        return user.getRole();
    }
    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }
    //spring Security에서 /login은 따로 관리한다 ->
    //하지만 SecurityConfig 를 작성하자마자 /login 관리하던게 풀림

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }
    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }


    @PostMapping("/join")
    public String join(User user){

        System.out.println(user);
        System.out.println(user.getUsername());
        user.setRole("ROLE_ADMIN");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);

        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){

        return "개인정보";
    }
}
