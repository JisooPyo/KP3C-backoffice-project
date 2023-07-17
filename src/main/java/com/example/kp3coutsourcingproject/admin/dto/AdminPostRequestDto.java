package com.example.kp3coutsourcingproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPostRequestDto {
    @NotBlank
    private String content; // 게시글 내용
}
