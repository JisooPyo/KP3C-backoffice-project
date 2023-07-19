package com.example.kp3coutsourcingproject.user.dto;

import com.example.kp3coutsourcingproject.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowUserDto {
    private Long id;
    private String username;
    private String nickname;
    private String imageUrl;

    public FollowUserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
