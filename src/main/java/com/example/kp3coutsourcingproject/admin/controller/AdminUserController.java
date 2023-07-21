package com.example.kp3coutsourcingproject.admin.controller;

import com.example.kp3coutsourcingproject.admin.dto.AdminUserResponseDto;
import com.example.kp3coutsourcingproject.admin.dto.AdminUserRoleRequestDto;
import com.example.kp3coutsourcingproject.admin.service.AdminUserService;
import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.user.dto.ProfileRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<AdminUserResponseDto> getUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        return adminUserService.getUsers(userDetails.getUser(),
                page - 1, size, sortBy, isAsc);
    }

    /* 회원 1명만 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<AdminUserResponseDto> getUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AdminUserResponseDto result = adminUserService.getUser(id, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    /* 회원 정보 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<AdminUserResponseDto> updateUserProfile(
            @PathVariable Long id,
            @RequestBody ProfileRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AdminUserResponseDto result = adminUserService.updateUserProfile(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    /* 회원 권한 승격(변경) */
    @PutMapping("/{id}/promote")
    public ResponseEntity<ApiResponseDto> updateUserRole(
            @PathVariable Long id,
            @RequestBody AdminUserRoleRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        adminUserService.updateUserRole(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 권한 변경 완료", HttpStatus.OK.value())
        );
    }

    /* 회원 비밀번호 변경(강제) */
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponseDto> updateUserPassword(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String pw = adminUserService.updateUserPassword(id, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 권한 변경 완료, 비밀번호: " + pw, HttpStatus.OK.value())
        );
    }

    /* 회원 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        adminUserService.deleteUser(id, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 삭제 완료", HttpStatus.OK.value())
        );
    }

    /* 회원 차단 */
    @PutMapping("/{id}/block")
    public ResponseEntity<ApiResponseDto> blockUser(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        adminUserService.blockUser(id, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 차단 완료", HttpStatus.OK.value())
        );
    }
}
