package com.example.kp3coutsourcingproject.admin.controller;

import com.example.kp3coutsourcingproject.admin.dto.AdminNoticeResponseDto;
import com.example.kp3coutsourcingproject.admin.service.AdminPostService;
import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import com.example.kp3coutsourcingproject.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kp3c/manage/post")
@RequiredArgsConstructor
public class AdminPostController {
    private final AdminPostService adminPostService;

    /* 전체 게시글 조회 */
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PostResponseDto> results = adminPostService.getPosts(userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }

    /* 게시글 1개 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PostResponseDto result = adminPostService.getPost(id, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    /* 게시글 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PostResponseDto result = adminPostService.updatePost(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        adminPostService.deletePost(id, userDetails.getUser());

        return ResponseEntity.ok().body(
                new ApiResponseDto("삭제가 완료되었습니다.", HttpStatus.OK.value())
        );
    }


}
