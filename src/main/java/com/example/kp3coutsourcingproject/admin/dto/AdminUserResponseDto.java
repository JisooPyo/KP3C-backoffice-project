package com.example.kp3coutsourcingproject.admin.dto;

import com.example.kp3coutsourcingproject.user.dto.ProfileRequestDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String introduction;
    private String imageUrl;
    private UserRoleEnum role;

    public AdminUserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.imageUrl = user.getImageFile();
        this.role = user.getRole();
    }
}
