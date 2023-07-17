package com.example.kp3coutsourcingproject.admin.dto;

import lombok.Getter;

@Getter
public class AdminUserResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String introduction;
    private String email;

    public AdminUserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
        this.introduction = user.getIntroduction();
        this.introduction = user.getEmail();
    }
}
