package com.example.kp3coutsourcingproject.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


	public User(String username, String nickname, String password, String introduction, String email, UserRoleEnum role) {
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.introduction = introduction;
		this.email = email;
		this.role = role;
	}
}