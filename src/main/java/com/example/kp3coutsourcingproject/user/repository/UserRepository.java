package com.example.kp3coutsourcingproject.user.repository;

import com.example.kp3coutsourcingproject.user.entity.SocialType;
import com.example.kp3coutsourcingproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);

	Optional<User> findByKakaoId(Long kakaoId);

	Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
	// OAuth2 로그인 구현시 사용하는 메소드
}
