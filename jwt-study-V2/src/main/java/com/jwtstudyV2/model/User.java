package com.jwtstudyV2.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String refreshToken;
    @Enumerated(EnumType.STRING)
    private Role roles;

    public User(Long id, String username, String password, String refreshToken, Role roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }


    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
