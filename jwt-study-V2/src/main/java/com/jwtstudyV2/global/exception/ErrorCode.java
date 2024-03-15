package com.jwtstudyV2.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();
    HttpStatus getHttpStatus();
    int getCode();
    String getMessage();
}
