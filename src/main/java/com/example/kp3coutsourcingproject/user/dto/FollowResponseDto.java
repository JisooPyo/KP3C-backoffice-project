package com.example.kp3coutsourcingproject.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowResponseDto {
    private boolean isFollowing;

    public FollowResponseDto(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }
}
