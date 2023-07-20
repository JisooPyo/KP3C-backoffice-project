package com.example.kp3coutsourcingproject.redis.controller;

import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.post.dto.PostResponseDto;
import com.example.kp3coutsourcingproject.redis.service.RedisTimelineService;
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
public class RedisTimelineController {
    private final RedisTimelineService redisTimelineService;

    /* 타임라인(피드) 게시글 조회 */
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getTimelinePosts(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<PostResponseDto> results = redisTimelineService.getTimelinePosts(userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }
}
