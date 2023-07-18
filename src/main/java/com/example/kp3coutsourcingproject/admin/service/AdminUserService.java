package com.example.kp3coutsourcingproject.admin.service;

import com.example.kp3coutsourcingproject.admin.dto.AdminUserRequestDto;
import com.example.kp3coutsourcingproject.admin.dto.AdminUserRoleRequestDto;
import com.example.kp3coutsourcingproject.user.dto.ProfileDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    public List<ProfileDto> getUsers(User user) {
        return null;
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
