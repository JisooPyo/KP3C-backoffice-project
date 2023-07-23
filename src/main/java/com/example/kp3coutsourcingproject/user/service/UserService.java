package com.example.kp3coutsourcingproject.user.service;

import com.example.kp3coutsourcingproject.common.exception.CustomException;
import com.example.kp3coutsourcingproject.common.exception.ErrorCode;
import com.example.kp3coutsourcingproject.common.file.FileStore;
import com.example.kp3coutsourcingproject.common.file.UploadFile;
import com.example.kp3coutsourcingproject.common.jwt.JwtUtil;
import com.example.kp3coutsourcingproject.common.redis.RedisUtils;
import com.example.kp3coutsourcingproject.user.dto.SignupRequestDto;
import com.example.kp3coutsourcingproject.user.dto.UserProfileRequestDto;
import com.example.kp3coutsourcingproject.user.dto.UserProfileResponseDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final FileStore fileStore;
	private final JwtUtil jwtUtil;
	private final RedisUtils redisUtils;

	// ADMIN_TOKEN
	private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


	//회원가입 메서드 - 회원가입 완료버튼을 눌렀을 때 동작할 메서드
	//회원가입을 위해 요청받은 requestBody 내 정보(userId,username,password,introduce,email)을 이용하여 계정 생성
	//userId, Email은 중복 불가능
	@Transactional
	public void signup(SignupRequestDto requestDto) throws IOException {
		UploadFile storeImageFile = fileStore.storeFile(requestDto.getImageFile());

		String username = requestDto.getUsername();
		String nickname = requestDto.getNickname();
		String password = passwordEncoder.encode(requestDto.getPassword());
		String introduce = requestDto.getIntroduction();
		String email = requestDto.getEmail();
		String imageFile = storeImageFile.getStoreFileName();

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
		User user = new User(username, nickname, password, introduce, email, role, imageFile);
		userRepository.save(user);
	}

	@Transactional
	public void logout(HttpServletResponse res, String accessToken) throws IOException {
		if(!jwtUtil.validateToken(res, accessToken)) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
		Claims userInfo = jwtUtil.getUserInfoFromToken(accessToken);
		String userEmail = userInfo.getSubject();

		redisUtils.delete(userEmail); // refresh token 삭제

		redisUtils.put(accessToken, "logout", JwtUtil.ACCESS_TOKEN_TIME); // blacklist 에 등록
	}

	// 프로필 조회
	public UserProfileResponseDto getUserProfile(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("해당 Id를 찾을 수 없습니다. : " + userId));
		return new UserProfileResponseDto(user);
	}

	// 프로필 수정
	public UserProfileResponseDto updateUserProfile(Long userId, UserProfileRequestDto requestDto) throws IOException { // 프로필 폼에서 받은 데이터를 넣어주어야 함
		UploadFile storeImageFile = fileStore.storeFile(requestDto.getImageFile());

		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			// 사용자 정보 업데이트
			user.setNickname(requestDto.getNickname());
			user.setIntroduction(requestDto.getIntroduction());
			user.setImageFile(storeImageFile.getStoreFileName()); // 서버용 파일이름으로 저장

			User updatedUser = userRepository.save(user);
			return new UserProfileResponseDto(updatedUser);
		}
		return null;
	}

}
