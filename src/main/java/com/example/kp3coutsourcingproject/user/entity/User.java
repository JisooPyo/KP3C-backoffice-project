package com.example.kp3coutsourcingproject.user.entity;

import com.nimbusds.oauth2.sdk.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	// nickname이 null로 들어오면 username과 같은 값을 넣도록 하고 싶음.
	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String password;

	// nullable = false 설정을 안해주면 어떻게 되지? 소개 안 쓰고 싶은 사람도 있는데!
	@Column(nullable = false)
	private String introduction;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role;

	@OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
	private List<Follow> followList = new ArrayList<>();

	@OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
	private List<Follow> followingList = new ArrayList<>();
	private Long kakaoId;

	/*소셜 로그인에 필요해서 넣은부분입니다. - 종우*/


	@Enumerated(EnumType.STRING)
	private SocialType socialType; //kakao, naver, google
	private String socialId; // 로그인한 소셜 타입의 식별자 값
	private String refreshToken; //리프레시 토큰

	public void authorizeUser() {
		this.role =UserRoleEnum.USER;
	}
	//비밀번호 암호화 메소드
	public void passwordEncode(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(this.password);
	}
	/*소셜 로그인에 필요해서 넣은부분입니다.*/

	public User(String username, String nickname, String password,
				String introduction, String email, UserRoleEnum role) {
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.introduction = introduction;
		this.email = email;
		this.role = role;
	}

	public User(String username, String nickname, String password,
				String introduction, String email, UserRoleEnum role , Long kakaoId) {
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.introduction = introduction;
		this.email = email;
		this.role = role;
		this.kakaoId = kakaoId;
	}

	public User kakaoIdUpdate(Long kakaoId) {
		this.kakaoId = kakaoId;
		return this;
	}
}
