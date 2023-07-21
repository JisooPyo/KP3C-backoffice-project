package com.example.kp3coutsourcingproject.post.dto;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.file.Post_Image;
import com.example.kp3coutsourcingproject.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PostResponseDto extends ApiResponseDto {
	private Long id;
	private String nickname;
	private String content;
	private LocalDateTime createdAt;
	private List<Post_Image> imageUrlList;


	public PostResponseDto(Post post) {
		this.id = post.getId();
		this.nickname = post.getUser().getNickname();
		this.content = post.getContent();
		this.createdAt = post.getCreatedAt();
		this.imageUrlList = post.getImagetList();
	}
}
