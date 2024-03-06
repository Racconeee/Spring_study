package com.spring_security.config.auth;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.spring_security.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;


//PrincipalDetailsService , loadUserByUsername 를 굳이 오버라이드 해서 사용하는 이유
//PrincipalOauth2UserService ,loadUser 를 굳이 오버라이드 해서 사용하는 이유
//2개의 클래스가 모두 같은 객체인 PrincipalDetails을 반환하기 위해서이다.
//PrincipalDetails을 반환해야 하나의 클래스로 기본로그인 , Oauth로그인이 가능하기 때문에
// Security Session => Authentication => UserDetails(PrincipalDetails)
@RequiredArgsConstructor
@Data
public class PrincipalDetails implements UserDetails , OAuth2User {

    private User user;
    private Map<String , Object> attributes;

    //일반 로그인은 user 정보만 받고
    public PrincipalDetails(User user) {
        this.user = user;
    }
    //OAuth로그인은 Map<String , Object> 정보까지 받는다
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }


    //해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }
    //비밀번호
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    //User의 이메일도 되지만 PK값을 넘기면 중복을 없앨수 있다
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정 만료 여부
    //true : 만료 안됨
    //false : 만료
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠김 여부
    //true : 잠기지않음
    //false : 잠김
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부
    //true : 만료 안됨
    //false : 만료
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //1년동안 회원이 로그인을 안하면 휴먼 계정으로 변경
    //true : 활성화
    //false : 비활성화
    @Override
    public boolean isEnabled() {
        //User Entity에 Timestamp loginDate 만들고
        //현재시간 - 로그인시간 => 1년 초과하면 return false; 하는 과정으로
        //휴면 계정을 판단할 수 있다.
        return true;
    }

    //Controller에서 @AuthenticationPrincipal을 사용하여 PrincipalDetails객체를 가지고 들어오면
    //당연하기 principalDetails.getAttributes(); 을 했을때 {끼룩=끼룩} 이 반환된다.

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put("끼룩" ,"끼룩");
        return map;
    }
    @Override
    public String getName() {
        return null;
    }
}
