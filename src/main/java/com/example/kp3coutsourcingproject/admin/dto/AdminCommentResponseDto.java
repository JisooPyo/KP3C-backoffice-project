package com.example.kp3coutsourcingproject.admin.dto;

import lombok.Getter;

@Getter
public class AdminCommentResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String content;
    private String createdAt;

    public AdminCommentResponseDto(Post post) {

        this.id = post.getId();
        this.username = post.getUsername();
        this.nickname = post.getNickname();

        this.content = post.getContent();

        this.createdAt = post.getCreatedAt();
    }
}
