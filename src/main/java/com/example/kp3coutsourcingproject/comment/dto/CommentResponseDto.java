package com.example.kp3coutsourcingproject.comment.dto;

import com.example.kp3coutsourcingproject.comment.entity.Comment;
import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto extends ApiResponseDto {
	private Long id;
	private String nickname;
	private String username;
	private LocalDateTime createdAt;
	private String content;

	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();
		this.nickname = comment.getUser().getNickname();
		this.username = comment.getUser().getUsername();
		this.createdAt = comment.getCreatedAt();
		this.content = comment.getContent();
	}
}
