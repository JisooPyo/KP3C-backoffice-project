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
import java.util.Optional;

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

        // 유저 전체 찾아서 responseDto로 반환
        List<User> users = userRepository.findAll().stream().toList();
        return users.stream().map(AdminUserResponseDto::new).toList();
    }

    public ProfileDto getUser(Long userId, User user) {
        // 유저의 권한 확인
        UserRoleEnum userRoleEnum = user.getRole();
        if (userRoleEnum != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        // 회원이 존재하는지 확인
        User findUser = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 유저는 존재하지 않습니다.")
        );
        return new ProfileDto(findUser);
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
