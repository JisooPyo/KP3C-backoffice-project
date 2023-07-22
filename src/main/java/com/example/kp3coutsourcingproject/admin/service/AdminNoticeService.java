package com.example.kp3coutsourcingproject.admin.service;

import com.example.kp3coutsourcingproject.admin.dto.AdminNoticeResponseDto;
import com.example.kp3coutsourcingproject.admin.entity.Notice;
import com.example.kp3coutsourcingproject.admin.repository.AdminNoticeRepository;
import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminNoticeService {
    private final AdminNoticeRepository adminNoticeRepository;

    public AdminNoticeResponseDto createNotice(PostRequestDto requestDto, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        Notice notice = new Notice(requestDto, admin);
        adminNoticeRepository.save(notice);

        return new AdminNoticeResponseDto(notice);
    }

    public Page<AdminNoticeResponseDto> getNotices(User admin, int page, int size, String sortBy, boolean isAsc) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }
        // 페이징 기능
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Notice> noticeList = adminNoticeRepository.findAll(pageable);
        return noticeList.map(AdminNoticeResponseDto::new);
    }

    public AdminNoticeResponseDto getNotice(Long noticeId, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        Notice findNotice = findNotice(noticeId);
        return new AdminNoticeResponseDto(findNotice);
    }

    public AdminNoticeResponseDto updateNotice(Long noticeId, PostRequestDto requestDto, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        Notice findNotice = findNotice(noticeId);
        findNotice.update(requestDto);

        return new AdminNoticeResponseDto(findNotice);
    }

    public void deleteNotice(Long noticeId, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        Notice findNotice = findNotice(noticeId);
        adminNoticeRepository.delete(findNotice);
    }

    private boolean isAdmin(User admin) {
        UserRoleEnum userRoleEnum = admin.getRole();
        if (userRoleEnum != UserRoleEnum.ADMIN) return false;
        return true;
    }

    private Notice findNotice(Long noticeId) {
        return adminNoticeRepository.findById(noticeId).orElseThrow(() ->
                new IllegalArgumentException("해당 공지사항은 존재하지 않습니다.")
        );
    }
}
