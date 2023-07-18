package com.example.kp3coutsourcingproject.admin.controller;

import com.example.kp3coutsourcingproject.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kp3c/manage")
@RequiredArgsConstructor
public class AdminPostController {
    private final AdminService adminService;

    @GetMapping("/posts")
    public void getPosts() { /* 전체 게시글 조회 */ }

    @PutMapping("/posts/{post_id}")
    public void updatePost() { /* 게시글 수정 */ }

    @DeleteMapping("/posts/{post_id}")
    public void deletePost() { /* 게시글 삭제 */ }

    @PostMapping("/notice")
    public void postNotice() { /* 공지 작성 */ }

    @PutMapping("/notices/{notice_id}")
    public void updateNotice() { /* 공지 작성 */ }

    @DeleteMapping("/notices/{notice_id}")
    public void deleteNotice() { /* 공지 삭제 */ }
}
