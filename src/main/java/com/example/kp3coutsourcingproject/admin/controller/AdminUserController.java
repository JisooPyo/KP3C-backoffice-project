package com.example.kp3coutsourcingproject.admin.controller;

import com.example.kp3coutsourcingproject.admin.service.AdminService;
import com.example.kp3coutsourcingproject.user.dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kp3c/manage")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<ProfileDto>> getUsers() { /* 전체 유저 조회 */
        return null;
    }

    @PutMapping("/users/{user_id}")
    public void updateUserInfo() { /* 회원 정보 수정 */

    }

    @PutMapping("/users/{user_id}/promote?role={role}")
    public void promoteUserAdmin() { /* 회원 권한 승격 */ }

    @PutMapping("/users/{user_id}/block")
    public void blockUser() { /* 회원 차단 */ }

    @DeleteMapping("/users/{user_id}")
    public void deleteUser() { /* 회원 삭제 */ }
}
