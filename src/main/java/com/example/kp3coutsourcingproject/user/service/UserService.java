package com.example.kp3coutsourcingproject.user.service;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.user.dto.SignupRequestDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
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
}
