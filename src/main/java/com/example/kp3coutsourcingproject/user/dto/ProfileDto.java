package com.example.kp3coutsourcingproject.user.dto;

import com.example.kp3coutsourcingproject.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {
    private String username;
    private String nickname;
    private String introduction;
    private String imageUrl;
    private int followerCount;
    private int followingCount;

    public ProfileDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.followerCount = user.getFollowList().size();
        this.followingCount = user.getFollowingList().size();
    }
}
