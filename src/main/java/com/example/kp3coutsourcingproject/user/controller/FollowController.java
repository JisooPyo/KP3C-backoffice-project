package com.example.kp3coutsourcingproject.user.controller;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.user.dto.FollowResponseDto;
import com.example.kp3coutsourcingproject.user.dto.ProfileDto;
import com.example.kp3coutsourcingproject.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kp3c")
public class FollowController {

    private final FollowService followService;

    @GetMapping("/users/{username}/followers")
    public ResponseEntity<List<ProfileDto>> getFollowers(@PathVariable String username) {
        List<ProfileDto> followers = followService.getFollowers(username);
        return ResponseEntity.ok().body(followers);
    }

    @GetMapping("/users/{username}/following")
    public ResponseEntity<List<ProfileDto>> getFollowing(@PathVariable String username) {
        List<ProfileDto> following = followService.getFollowing(username);
        return ResponseEntity.ok().body(following);
    }

    @PostMapping("/user/follow/{username}")
    public ResponseEntity<ApiResponseDto> follow(@PathVariable String username, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.follow(username, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("팔로우 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/user/unfollow/{username}")
    public ResponseEntity<ApiResponseDto> unfollow(@PathVariable String username, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.unfollow(username, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("언팔로우 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/user/following/{username}")
    public ResponseEntity<FollowResponseDto> isFollowing(@PathVariable String username, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean isFollowing = followService.isFollowing(username, userDetails.getUser());
        return ResponseEntity.ok().body(new FollowResponseDto(isFollowing));
    }
}
