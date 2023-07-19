package com.example.kp3coutsourcingproject.user.dto;

import com.example.kp3coutsourcingproject.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserProfileResponseDto {
    private String nickname;
    private String introduction;
    private String imageFile;

    public UserProfileResponseDto(User user){
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.imageFile = user.getImageFile();
    }

}
