package com.example.kp3coutsourcingproject.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostRequestDto {
	private Long parentId;
	@NotNull(message = "내용을 입력하세요.")
	private String content;
}
