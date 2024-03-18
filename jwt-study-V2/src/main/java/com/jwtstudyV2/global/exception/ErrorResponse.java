package com.jwtstudyV2.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final boolean success = false;
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public static ErrorResponse of(HttpStatus httpStatus, int code, String message){
        return ErrorResponse.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }


}
