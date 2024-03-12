package com.jwtstudyV1.model;

import lombok.Getter;

@Getter
public enum Role {

    USER("사용자") ,ADMIN("관리자") , MANAGER("매니저");


    private final String message;

    Role(String message) {
        this.message = message + "권한을 가지고 있습니다.";
    }
}
