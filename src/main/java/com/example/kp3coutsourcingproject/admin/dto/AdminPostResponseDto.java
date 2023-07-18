package com.example.kp3coutsourcingproject.admin.dto;

import com.example.kp3coutsourcingproject.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminPostResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public AdminPostResponseDto(Post post) {

        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.username = post.getUser().getNickname();

        this.content = post.getContent();

        this.createdAt = post.getCreatedAt();
    }
}
