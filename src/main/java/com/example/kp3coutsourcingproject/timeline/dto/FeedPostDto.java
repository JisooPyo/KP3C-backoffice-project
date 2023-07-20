package com.example.kp3coutsourcingproject.timeline.dto;

import com.example.kp3coutsourcingproject.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeedPostDto {
    /* 타임라인(피드)에 표시될 정보들 */
    private Long postId;
    private String username;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public FeedPostDto(Post post) {
        this.postId = post.getId();
        this.username = post.getUser().getUsername();
        this.nickname = post.getUser().getNickname();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
    }
}
