package com.example.kp3coutsourcingproject.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SignupRequestDto {
	@NotBlank
	@Size(min = 4, max = 10, message= "userID는 최소 4자 이상, 10자 이하여야 합니다.")
	@Pattern(regexp = "^[a-z0-9]*$", message = "userID는 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.")
	private String username;

	@NotBlank
	private String nickname;

	@NotBlank
	@Size(min =8, max = 15,message= "비밀번호는 최소 8자 이상, 15자 이하여야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]*$", message = "비밀번호는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성되어야 합니다.")
	private String password;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String introduction;

	@NotNull
	private MultipartFile imageFile;

	private boolean admin = false;
	private String adminToken = "";
}
