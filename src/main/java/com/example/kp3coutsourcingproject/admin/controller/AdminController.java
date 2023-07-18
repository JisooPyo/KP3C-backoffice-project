package com.example.kp3coutsourcingproject.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kp3c/manage")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("")
    public void adminPage() { /* 관리자 페이지 */ }
}
