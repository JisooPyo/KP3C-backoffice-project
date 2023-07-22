package com.example.kp3coutsourcingproject.user.dto;

import com.example.kp3coutsourcingproject.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserProfileResponseDto {

    private String username;
    private String nickname;
    private String introduction;
    private int followingCount;
    private int followerCount;
    private String imageFile;

    public UserProfileResponseDto(User user){
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.followingCount = user.getFollowingCount();
        this.followerCount = user.getFollowerCount();
        this.imageFile = user.getImageFile();
    }

}
