package com.example.kp3coutsourcingproject.admin.dto;

import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserRoleRequestDto {

    @NotBlank
    private UserRoleEnum role;
}
