package com.jwtstudyV2.model;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Data
@RequiredArgsConstructor
public class UserDto {

    private String username;
    private String password;

}
