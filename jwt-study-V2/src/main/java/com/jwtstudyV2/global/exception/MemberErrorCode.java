package com.jwtstudyV2.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND,404,"존재하지 않는 사용자입니다"),
    MEMBER_ALREADY_EXISTS_ERROR(HttpStatus.CONFLICT,409, "이미 존재하는 회원입니다"),
    INACTIVE_USER_ERROR(HttpStatus.FORBIDDEN, 403,"권한이 없는 사용자입니다"),
    PASSWORD_MISMATCH_ERROR(HttpStatus.UNAUTHORIZED, 401, "비밀번호가 일치하지 않습니다");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
