package com.example.kp3coutsourcingproject.admin.service;

import com.example.kp3coutsourcingproject.admin.dto.AdminUserResponseDto;
import com.example.kp3coutsourcingproject.admin.dto.AdminUserRoleRequestDto;
import com.example.kp3coutsourcingproject.user.dto.ProfileRequestDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AdminUserResponseDto> getUsers(User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        // 유저 전체 찾아서 responseDto로 반환
        List<User> users = userRepository.findAll().stream().toList();
        return users.stream().map(AdminUserResponseDto::new).toList();
    }

    public AdminUserResponseDto getUser(Long userId, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        // 회원이 존재하는지 확인
        User findUser = findUser(userId);
        return new AdminUserResponseDto(findUser);
    }

    public AdminUserResponseDto updateUserProfile(Long userId, ProfileRequestDto requestDto, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        User findUser = findUser(userId);
        // findUser가 관리인이면 자신만 수정할 수 있게 하기
        if (isAdmin(findUser)) {
            if (findUser.getId() != admin.getId()) {
                throw new IllegalArgumentException("관리자 정보는 본인만 수정할 수 있습니다.");
            }
        }
        findUser.update(requestDto);
        return new AdminUserResponseDto(findUser);
    }

    public void promoteUserRole(Long userId, AdminUserRoleRequestDto requestDto, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        User findUser = findUser(userId);
        // findUser가 관리인이면 권한 수정 불가능
        if (isAdmin(findUser)) {
            throw new IllegalArgumentException("권한을 수정할 수 없습니다.");
        }

        findUser.setRole(requestDto.getRole()); // 권한 변경
    }

    public String updateUserPassword(Long userId, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        // 비밀번호 임의설정
        String newPassword = createPassword(15);

        User findUser = findUser(userId);
        // findUser가 관리인이면 비밀번호 수정 불가능
        if (isAdmin(findUser)) {
            throw new IllegalArgumentException("비밀번호를 수정할 수 없습니다.");
        }
        findUser.setPassword(passwordEncoder.encode(newPassword));

        return newPassword;
    }

    public void blockUser(Long userId, User admin) {
    }

    public void deleteUser(Long userId, User admin) {
    }

    private boolean isAdmin(User admin) {
        UserRoleEnum userRoleEnum = admin.getRole();
        if (userRoleEnum != UserRoleEnum.ADMIN) return false;
        return true;
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 유저는 존재하지 않습니다.")
        );
    }

    private String createPassword(int length) {
        // 랜덤으로 생성
        return RandomStringUtils.random(length, 33, 126, false, false);
    }
}
