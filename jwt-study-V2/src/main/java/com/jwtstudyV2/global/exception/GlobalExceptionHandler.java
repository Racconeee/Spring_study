package com.jwtstudyV2.global.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    /*
    * CustomException 에는 message 필드가 정의 되어있지않은데 어떻게 getMessage()를 하나요 ?
    * RuntimeException 은 Throwable class를 상속받고 있는데 여기에 String getMessage()가 정의 되어있어 사용이 가능하다.
    * */
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String , String>> handlerCustomException(CustomException ex){
        Map<String , String > response = new HashMap<>();
        response.put("error" , ex.getMessage());
        response.put("error time" , new Date(System.currentTimeMillis()) +"");
        return new ResponseEntity<>(response , ex.getHttpStatus());
    }

    //MemberException class내용을 기반으로 에러 메세지 생성해서 보냄
    //throw new MemberException(MemberErrorCode.MEMBER_ALREADY_EXISTS_ERROR); 이런 형식으로 보낸다.
    @ExceptionHandler(MemberException.class)
    public ResponseEntity handleException(MemberException e){
        ErrorCode errorCode = e.getMemberErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getHttpStatus() , errorCode.getCode(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }


}
