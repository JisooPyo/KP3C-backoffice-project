package com.example.kp3coutsourcingproject.redis.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash("timeline")
public class RedisTimeline {
    @Id
    private String userId; // key
    private String postId;

    @Builder
    public RedisTimeline(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
