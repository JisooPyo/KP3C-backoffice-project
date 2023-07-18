package com.example.kp3coutsourcingproject.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequestDto {
    @NotBlank
    private String nickname;
    private String introduction;
    @NotBlank
    private String imageUrl;
}
