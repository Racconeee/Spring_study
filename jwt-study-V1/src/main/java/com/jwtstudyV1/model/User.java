package com.jwtstudyV1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//@vaild 는 일반적으로 dto 계층에서 적용
@Entity
@Data
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank(message = "이메일은 필수 값입니다.")
    @Column(nullable = false , length = 10)
    @Size(max = 10 ,message = "10글자까지만 입력이 가능합니다.")
    private String username;

    @NotBlank(message = "비빌번호는 필수 값입니다.")
    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Role roles;


    @Builder
    private User(String username , String password){
        this.username = username;
        this.password = password;
    }

    public List<String> getRoleList(){
        if(this.roles != null){
            return Collections.singletonList(this.roles.name());
        }
        return Collections.emptyList();
    }

}
