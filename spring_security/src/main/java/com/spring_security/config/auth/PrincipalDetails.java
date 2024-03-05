package com.spring_security.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.spring_security.model.User;


// Security Session => Authentication => UserDetails(PrincipalDetails)
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final User user;

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
}
