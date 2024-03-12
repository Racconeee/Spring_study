package com.jwtstudyV1.auth;

import com.jwtstudyV1.model.Role;
import com.jwtstudyV1.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


//객체의 권한을 확인하는 class
//유저의 권한이 저장되어있다.
@Configuration
@RequiredArgsConstructor
@Getter
public class PrincipalDetails implements UserDetails {

    public PrincipalDetails(User user) {
        this.user = user;
    }

    private User user;
    private Collection<GrantedAuthority> authorities;
    @Override

    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("PrincipalDetails 실행 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ" );
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRoles().toString())); //ROLE을 붙여야하는데 왜 붙여야하는지 ?
        System.out.println(authorities);
        return authorities;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
