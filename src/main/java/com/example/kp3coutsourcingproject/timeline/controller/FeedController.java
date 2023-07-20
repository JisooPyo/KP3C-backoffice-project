package com.example.kp3coutsourcingproject.timeline.controller;

import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.timeline.dto.FeedPostDto;
import com.example.kp3coutsourcingproject.timeline.dto.FeedResponseDto;
import com.example.kp3coutsourcingproject.timeline.service.FeedService;
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
public class FeedController {
    private final FeedService feedService;

    /* 타임라인(피드) 게시글 조회 */
    @GetMapping("/my")
    public ResponseEntity<List<FeedPostDto>> getMyLastCreatedPosts(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<FeedPostDto> results = feedService.getMyLastCreatedPosts(userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedResponseDto> getMyFeed(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FeedResponseDto result = feedService.getMyFeed(userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }
}
