package com.example.kp3coutsourcingproject.comment.dto;

import com.example.kp3coutsourcingproject.comment.entity.Comment;
import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto extends ApiResponseDto {
	private Long id;
	private String nickname;
	private String content;
	private LocalDateTime createdAt;

	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();
		this.nickname = comment.getUser().getNickname();
		this.content = comment.getContent();
		this.createdAt = comment.getCreatedAt();
	}
}
