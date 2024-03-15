package com.jwtstudyV2.global.exception;


@Getter
public class MemberException extends CustomException{


    private final MemberErrorCode memberErrorCode;

    public MemberException(MemberErrorCode memberErrorCode) {
        super(memberErrorCode.getMessage());
        this.memberErrorCode = memberErrorCode;
    }

    public MemberErrorCode getMemberErrorCode() {
        return memberErrorCode;
    }
}