package com.example.kp3coutsourcingproject.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostRequestDto {
	@NotNull(message = "내용을 입력하세요.")
	private String content;
}
