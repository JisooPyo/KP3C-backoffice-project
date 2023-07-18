package com.example.kp3coutsourcingproject.admin.dto;

import com.example.kp3coutsourcingproject.user.dto.ProfileDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserResponseDto extends ProfileDto {

    private Long id;
    private UserRoleEnum role;

    public AdminUserResponseDto(User user) {
        super(user);
        this.id = user.getId();
        this.role = user.getRole();
    }
}
