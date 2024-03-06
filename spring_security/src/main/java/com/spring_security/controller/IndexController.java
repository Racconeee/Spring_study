package com.spring_security.controller;

import com.spring_security.config.auth.PrincipalDetails;
import com.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    //Authentication 객체를 받아서 PrincipalDetails객체로 다운캐스팅
    //사실은 Authentication 객체를 받아서 UserDetails객체로 다운캐스팅 해야하는데 PrincipalDetails가 UserDetails를 구현햇으니
    //PrincipalDetails타입은 UserDetails타입이 될수 있다 따라서 PrincipalDetails타입으로 다운캐스팅 진행

    //일반적인 login을 하게되면  Authentication객체에 UserDetails 객체가 들어오게 된다
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication
            , @AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println("/test/login ============================");
        System.out.println("authentication" + authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication" + principalDetails.getUser());
        System.out.println("authentication" + userDetails.getUser());
        return "test 성공";
    }
    //Oauth login을 하게되면  Authentication객체에 OAuth2User 객체가 들어오게 된다
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testLogin(Authentication authentication
    , @AuthenticationPrincipal OAuth2User oAuth){
        System.out.println("/test/oauth/login ============================");
        System.out.println("authentication" + authentication.getPrincipal());
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication" + oAuth.getAttributes());
        return "OAuth 세션정보 저장하기";
    }


    //뷰 리졸버 설정 :templates(prefix) . mustache (suffix) 생략가능
    @GetMapping({"" , "/"})
    public String index(){
        return "index";
    }


    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails :" + principalDetails.getUser());
        System.out.println(principalDetails.getAttributes());
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
