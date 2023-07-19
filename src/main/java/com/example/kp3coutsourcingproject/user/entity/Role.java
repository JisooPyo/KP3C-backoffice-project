package com.example.kp3coutsourcingproject.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST"), USER("ROLE_USER");
    private final String key;
}

/*OAuth2 로그인 시 첫 로그인을 구분
* ADMIN은 ???
* ROLE을 붙여야 스프링 시큐리티에서 사용가능*/