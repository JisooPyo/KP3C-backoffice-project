package com.example.kp3coutsourcingproject.admin.controller;

import com.example.kp3coutsourcingproject.admin.service.AdminCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kp3c/manage")
@RequiredArgsConstructor
public class AdminCommentController {
    private final AdminCommentService adminCommentService;

    @GetMapping("/comments")
    public void getComments() { /* 전체 댓글 조회 */ }

    @PutMapping("/comments/{comment_id}")
    public void updateComment() { /* 댓글 수정 */ }

    @DeleteMapping("/comments/{comment_id}")
    public void deleteComment() { /* 댓글 삭제 */ }
}
