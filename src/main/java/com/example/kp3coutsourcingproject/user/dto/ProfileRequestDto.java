package com.example.kp3coutsourcingproject.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequestDto {
    private String username;
    private String nickname;
    private String introduction;
    private String imageUrl;
}
