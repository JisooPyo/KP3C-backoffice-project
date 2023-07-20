package com.example.kp3coutsourcingproject.timeline.controller;

import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.timeline.dto.FeedPostDto;
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
    private final FeedService timelineService;

    /* 타임라인(피드) 게시글 조회 */
    @GetMapping
    public ResponseEntity<List<FeedPostDto>> getTimelinePosts(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<FeedPostDto> results = timelineService.getTimelinePosts(userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }
}
