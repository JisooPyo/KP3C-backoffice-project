package com.example.kp3coutsourcingproject.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    TOKEN_NOT_FOUND(404, "해당 토큰이 존재하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(404, "액세스 토큰이 만료되어 재발급이 필요합니다."),
    EXPIRED_REFRESH_TOKEN(404, "리프레시 토큰이 만료되어 로그인이 필요합니다."),
    INVALID_TOKEN(404, "유효하지 않은 토큰입니다");

    private final int status;
    private final String message;
}
