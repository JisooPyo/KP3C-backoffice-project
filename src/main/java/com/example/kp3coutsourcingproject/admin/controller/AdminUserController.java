package com.example.kp3coutsourcingproject.admin.controller;

import com.example.kp3coutsourcingproject.admin.dto.AdminUserRequestDto;
import com.example.kp3coutsourcingproject.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kp3c/manage")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminService adminService;

    @GetMapping("/users")
    public void getUsers() { /* 전체 유저 조회 */ }

    @PutMapping("/users/{user_id}")
    public void updateUserInfo() {
        /* 회원 정보 수정 */
    }

    @PutMapping("/users/{user_id}/promote?role={role}")
    public void promoteUserAdmin() { /* 회원 권한 승격 */ }

    @PutMapping("/users/{user_id}/block")
    public void blockUser() { /* 회원 차단 */ }

    @DeleteMapping("/users/{user_id}")
    public void deleteUser() { /* 회원 삭제 */ }
}
