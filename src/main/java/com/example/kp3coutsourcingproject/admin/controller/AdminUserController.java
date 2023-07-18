package com.example.kp3coutsourcingproject.admin.controller;

import com.example.kp3coutsourcingproject.admin.dto.AdminUserRequestDto;
import com.example.kp3coutsourcingproject.admin.dto.AdminUserRoleRequestDto;
import com.example.kp3coutsourcingproject.admin.service.AdminUserService;
import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.user.dto.ProfileDto;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
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

    @GetMapping
    public ResponseEntity<List<ProfileDto>> getUsers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        /* 전체 유저 조회 */

        List<ProfileDto> profileDtos = adminUserService.getUsers(userDetails.getUser());

        return ResponseEntity.ok().body(profileDtos);
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<ProfileDto> updateUserInfo(
            @PathVariable(value = "user_id") Long userId,
            @RequestBody AdminUserRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        /* 회원 정보 수정 */

        ProfileDto profileDto = adminUserService.updateUserInfo(userId, requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(profileDto);
    }

    @PutMapping("/{user_id}/promote")
    public ResponseEntity<ApiResponseDto> promoteUserRole(
            @PathVariable(value="user_id") Long userId,
            @RequestBody AdminUserRoleRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {

        /* 회원 권한 승격(변경) */
        adminUserService.promoteUserRole(userId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 권한 변경 완료", HttpStatus.OK.value())
        );
    }

    @PutMapping("/{user_id}/block")
    public ResponseEntity<ApiResponseDto> blockUser(@PathVariable(value = "user_id") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        /* 회원 차단 */
        adminUserService.blockUser(userId, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 차단 완료", HttpStatus.OK.value())
        );
    }


    @DeleteMapping("/{user_id}")
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable(value = "user_id") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        /* 회원 삭제 */
        adminUserService.deleteUser(userId, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("회원 삭제 완료", HttpStatus.OK.value())
        );
    }
}
