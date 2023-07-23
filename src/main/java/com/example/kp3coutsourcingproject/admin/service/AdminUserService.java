package com.example.kp3coutsourcingproject.admin.service;

import com.example.kp3coutsourcingproject.admin.dto.AdminUserResponseDto;
import com.example.kp3coutsourcingproject.admin.dto.AdminUserRoleRequestDto;
import com.example.kp3coutsourcingproject.common.jwt.JwtUtil;
import com.example.kp3coutsourcingproject.common.redis.RedisUtils;
import com.example.kp3coutsourcingproject.post.entity.Post;
import com.example.kp3coutsourcingproject.post.repository.PostRepository;
import com.example.kp3coutsourcingproject.user.dto.ProfileRequestDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    private final RedisUtils redisUtils;

    public Page<AdminUserResponseDto> getUsers(User admin, int page, int size, String sortBy, boolean isAsc) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }
        // 페이징 기능
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 유저 전체 찾아서 responseDto로 반환
        Page<User> userList = userRepository.findAll(pageable);
        return userList.map(AdminUserResponseDto::new);
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

    public void updateUserRole(Long userId, AdminUserRoleRequestDto requestDto, User admin) {
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

    public void deleteUser(Long userId, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }
        User findUser = findUser(userId);
        if (isAdmin(findUser)) {
            throw new IllegalArgumentException("관리자는 삭제할 수 없습니다.");
        }
        // 게시글 삭제 -> 회원 삭제 순서
        List<Post> postList = postRepository.findAllByUser(findUser);
        for(Post post : postList) {
            postRepository.delete(post);
        }

        String accessToken = redisUtils.get(findUser.getEmail(), "access_token", String.class);
        redisUtils.put(accessToken, "delete forced", JwtUtil.ACCESS_TOKEN_TIME); // blacklist 에 등록

        redisUtils.delete(findUser.getEmail(), "refresh_token"); // refresh token 삭제
        redisUtils.delete(findUser.getEmail(), "access_token"); // access token 삭제
        userRepository.delete(findUser); // 회원 정보 삭제
    }

    public void blockUser(Long userId, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        User findUser = findUser(userId);
        findUser.setEnabled(false); // enable 불가능으로 전환

        String accessToken = redisUtils.get(findUser.getEmail(), "access_token", String.class);
        redisUtils.put(accessToken, "block", JwtUtil.ACCESS_TOKEN_TIME); // blacklist 에 등록

        redisUtils.delete(findUser.getEmail(), "refresh_token"); // refresh token 삭제
        redisUtils.delete(findUser.getEmail(), "access_token"); // access token 삭제
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
