package com.example.kp3coutsourcingproject.redis.entity;

import com.example.kp3coutsourcingproject.redis.dto.RedisPostDto;
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
    private RedisPostDto redisPostDto; // value
}
