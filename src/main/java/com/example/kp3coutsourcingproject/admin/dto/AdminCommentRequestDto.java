package com.example.kp3coutsourcingproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCommentRequestDto {
    @NotBlank
    private String content; // 댓글 내용
}
