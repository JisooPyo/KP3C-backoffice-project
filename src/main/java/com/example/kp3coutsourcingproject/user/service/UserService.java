package com.example.kp3coutsourcingproject.user.service;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.user.dto.ProfileDto;
import com.example.kp3coutsourcingproject.user.dto.SignupRequestDto;
import com.example.kp3coutsourcingproject.user.entity.Follow;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import com.example.kp3coutsourcingproject.user.repository.FollowRepository;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Component
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final PasswordEncoder passwordEncoder;

	// ADMIN_TOKEN
	private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


	//회원가입 메서드 - 회원가입 완료버튼을 눌렀을 때 동작할 메서드
	//회원가입을 위해 요청받은 requestBody 내 정보(userId,username,password,introduce,email)을 이용하여 계정 생성
	//userId, Email은 중복 불가능
	@Transactional
	public ApiResponseDto signup(SignupRequestDto requestDto, HttpServletResponse response) {
		String username = requestDto.getUsername();
		String nickname = requestDto.getNickname();
		String password = passwordEncoder.encode(requestDto.getPassword());
		String introduce = requestDto.getIntroduction();
		String email = requestDto.getEmail();

		// 아이디 중복 확인
		Optional<User> checkUserId = userRepository.findByUsername(username);
		// email 중복확인
		Optional<User> checkEmail = userRepository.findByEmail(email);

		if (checkUserId.isPresent()) {
			throw new DuplicateKeyException("이미 존재하는 ID입니다.");
		}

		if (checkEmail.isPresent()) {
			throw new DuplicateKeyException("이미 존재하는 이메일입니다.");
		}

		// 사용자 ROLE 확인
		UserRoleEnum role = UserRoleEnum.USER;
		if (requestDto.isAdmin()) {
			if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
				throw new BadCredentialsException("관리자 암호가 틀려 등록이 불가능합니다.");
			}
			role = UserRoleEnum.ADMIN;
		}
		// 사용자 등록
		User user = new User(username, nickname, password, introduce, email, role);
		userRepository.save(user);
		response.setStatus(200);
		return new ApiResponseDto("회원가입 완료", response.getStatus());
	}

	public List<ProfileDto> getFollowers(String username) {
		User user = findUser(username);
		return user.getFollowList()
				.stream()
				.map(Follow::getFollower)
				.map(ProfileDto::new)
				.toList();
	}

	public List<ProfileDto> getFollowing(String username) {
		User user = findUser(username);
		return user.getFollowingList()
				.stream()
				.map(Follow::getFollowee)
				.map(ProfileDto::new)
				.toList();
	}

	public void follow(String username, User user) {
		User follower = findUser(username);
		if(followRepository.existsByFollowerAndFollowee(follower, user)) {
			throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
		}
		followRepository.save(new Follow(follower, user));
	}

	public void unfollow(String username, User user) {
		User follower = findUser(username);
		Follow existedFollow = followRepository.findByFollowerAndFollowee(follower, user).orElseThrow(() ->
			new IllegalArgumentException("팔로우하지 않은 사용자입니다.")
		);
		followRepository.delete(existedFollow);
	}

	private User findUser(String username) {
		return userRepository.findByUsername(username).orElseThrow(() ->
				new IllegalArgumentException("존재하지 않는 사용자입니다.")
		);
	}
}
