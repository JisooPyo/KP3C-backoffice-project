package com.example.kp3coutsourcingproject.admin.controller;

import com.example.kp3coutsourcingproject.admin.dto.AdminUserResponseDto;
import com.example.kp3coutsourcingproject.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kp3c/manage")
@RequiredArgsConstructor
public class AdminController {



    @GetMapping("")
    public void adminPage() { /* 관리자 페이지 */ }






}
