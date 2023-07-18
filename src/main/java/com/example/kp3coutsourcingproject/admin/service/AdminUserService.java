package com.example.kp3coutsourcingproject.admin.service;

import com.example.kp3coutsourcingproject.admin.dto.AdminUserRequestDto;
import com.example.kp3coutsourcingproject.admin.dto.AdminUserResponseDto;
import com.example.kp3coutsourcingproject.admin.dto.AdminUserRoleRequestDto;
import com.example.kp3coutsourcingproject.user.dto.ProfileDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public List<AdminUserResponseDto> getUsers(User user) {

        // 유저의 권한 확인
        UserRoleEnum userRoleEnum = user.getRole();
        if (userRoleEnum != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        List<User> users = userRepository.findAll().stream().toList();
        return users.stream().map(AdminUserResponseDto::new).toList();
    }

    public ProfileDto updateUserInfo(Long userId, AdminUserRequestDto requestDto, User user) {
        return null;
    }

    public void promoteUserRole(Long userId, AdminUserRoleRequestDto requestDto, User user) {
    }

    public void blockUser(Long userId, User user) {
    }

    public void deleteUser(Long userId, User user) {
    }
}
