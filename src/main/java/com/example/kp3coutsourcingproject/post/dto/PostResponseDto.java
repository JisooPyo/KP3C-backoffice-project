package com.example.kp3coutsourcingproject.post.dto;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto extends ApiResponseDto {
	private Long id;
	private String username;
	private String nickname;
	private String content;
	private LocalDateTime createdAt;


	public PostResponseDto(Post post) {
		this.id = post.getId();
		this.username=post.getUser().getUsername();
		this.nickname = post.getUser().getNickname();
		this.content = post.getContent();
		this.createdAt = post.getCreatedAt();
	}
}
