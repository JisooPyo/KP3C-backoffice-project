package com.example.kp3coutsourcingproject.admin.controller;

import com.example.kp3coutsourcingproject.admin.dto.AdminNoticeResponseDto;
import com.example.kp3coutsourcingproject.admin.service.AdminNoticeService;
import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kp3c/manage/notice")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    /* 공지 작성 */
    @PostMapping
    public ResponseEntity<AdminNoticeResponseDto> createNotice(
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AdminNoticeResponseDto result = adminNoticeService.createNotice(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    /* 전체 공지 조회 */
    @GetMapping
    public ResponseEntity<List<AdminNoticeResponseDto>> getNotices(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AdminNoticeResponseDto> results = adminNoticeService.getNotices(userDetails.getUser());
        return ResponseEntity.ok().body(results);
    }

    /* 공지 1개 조회 */
    @GetMapping("/{notice_id}")
    public ResponseEntity<AdminNoticeResponseDto> getNotice(
            @PathVariable Long noticeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AdminNoticeResponseDto result = adminNoticeService.getNotice(noticeId, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    /* 공지 수정 */
    @PutMapping("/{notice_id}")
    public ResponseEntity<AdminNoticeResponseDto> updateNotice(
            @PathVariable Long noticeId,
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AdminNoticeResponseDto result = adminNoticeService.updateNotice(noticeId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    /* 공지 삭제 */
    @DeleteMapping("/{notice_id}")
    public ResponseEntity<ApiResponseDto> deleteNotice(
            @PathVariable Long noticeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        adminNoticeService.deleteNotice(noticeId, userDetails.getUser());
        return ResponseEntity.ok().body(
                new ApiResponseDto("삭제가 완료되었습니다.", HttpStatus.OK.value())
        );
    }
}
