package com.example.kp3coutsourcingproject.redis.controller;

import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.redis.dto.RedisPostDto;
import com.example.kp3coutsourcingproject.redis.service.RedisPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisPostController {
    private final RedisPostService redisPostService;

    /* 타임라인(피드) 게시글 조회 */
    @GetMapping("/my")
    public ResponseEntity<List<RedisPostDto>> getMyLastCreatedPosts(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<RedisPostDto> results = redisPostService.getMyLastCreatedPosts(userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }
}
