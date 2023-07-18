package com.example.kp3coutsourcingproject.admin.dto;

import com.example.kp3coutsourcingproject.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminCommentResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public AdminCommentResponseDto(Post post) {

        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.nickname = post.getUser().getNickname();

        this.content = post.getContent();

        this.createdAt = post.getCreatedAt();
    }
}
