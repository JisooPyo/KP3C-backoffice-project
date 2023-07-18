package com.example.kp3coutsourcingproject.admin.controller;

import com.example.kp3coutsourcingproject.admin.dto.AdminUserResponseDto;
import com.example.kp3coutsourcingproject.admin.dto.AdminUserRoleRequestDto;
import com.example.kp3coutsourcingproject.admin.service.AdminUserService;
import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.user.dto.ProfileRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kp3c/manage/user")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    /* 전체 회원 조회 */
    @GetMapping
    public ResponseEntity<List<AdminUserResponseDto>> getUsers(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<AdminUserResponseDto> results = adminUserService.getUsers(userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }

    /* 회원 1명만 조회 */
    @GetMapping("/{user_id}")
    public ResponseEntity<AdminUserResponseDto> getUser(
            @PathVariable(value = "user_id") Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AdminUserResponseDto result = adminUserService.getUser(userId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    /* 회원 정보 수정 */
    @PutMapping("/{user_id}")
    public ResponseEntity<AdminUserResponseDto> updateUserProfile(
            @PathVariable(value = "user_id") Long userId,
            @RequestBody ProfileRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AdminUserResponseDto result = adminUserService.updateUserProfile(userId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    /* 회원 권한 승격(변경) */
    @PutMapping("/{user_id}/promote")
    public ResponseEntity<ApiResponseDto> updateUserRole(
            @PathVariable(value = "user_id") Long userId,
            @RequestBody AdminUserRoleRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        adminUserService.promoteUserRole(userId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 권한 변경 완료", HttpStatus.OK.value())
        );
    }

    /* 회원 비밀번호 변경(강제) */
    @PutMapping("/{user_id}/password")
    public ResponseEntity<ApiResponseDto> updateUserPassword(
            @PathVariable(value = "user_id") Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String pw = adminUserService.updateUserPassword(userId, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 권한 변경 완료, 비밀번호: " + pw, HttpStatus.OK.value())
        );
    }

    /* 회원 차단 */
    @PutMapping("/{user_id}/block")
    public ResponseEntity<ApiResponseDto> blockUser(@PathVariable(value = "user_id") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        adminUserService.blockUser(userId, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 차단 완료", HttpStatus.OK.value())
        );
    }

    /* 회원 삭제 */
    @DeleteMapping("/{user_id}")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable(value = "user_id") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        adminUserService.deleteUser(userId, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 삭제 완료", HttpStatus.OK.value())
        );
    }
}
