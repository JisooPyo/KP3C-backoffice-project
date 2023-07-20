package com.example.kp3coutsourcingproject.feed.entity;

import com.example.kp3coutsourcingproject.feed.dto.FeedPostDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("redis_post")
public class RedisPost {
    @Id
    private String userId; // key
    private FeedPostDto feedPostDto; // value
}
