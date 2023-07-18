package com.example.kp3coutsourcingproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserRequestDto {

    @NotBlank
    private String nickname;
    @NotBlank
    private String password;
    private String introduction;
    @NotBlank
    private String email;
}
