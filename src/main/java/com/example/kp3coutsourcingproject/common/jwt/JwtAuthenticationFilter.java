package com.example.kp3coutsourcingproject.common.jwt;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.user.dto.LoginRequestDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtUtil jwtUtil;
	private final AuthenticationFailureHandler authenticationFailureHandler;

	//POST "api/user/login"을 로그인 인증필터의 url으로 설정
	public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthenticationFailureHandler authenticationFailureHandler) {
		this.jwtUtil = jwtUtil;
		this.authenticationFailureHandler = authenticationFailureHandler;

		setFilterProcessesUrl("/kp3c/user/login");
	}

	//로그인 시도 시 동작하는 메서드
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			//requestBody 요청 값을 LoginRequestDto 객체타입으로 변환
			LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

			//아이디, 비밀번호 입력내용을 체크.
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							requestDto.getUsername(),
							requestDto.getPassword(),
							null
					)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}


	// 인증 성공(로그인 성공) 시 동작하는 메서드
	// JWT 생성 및 client에 응답
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
		User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
		TokenDto token = jwtUtil.issueToken(user.getEmail(), user.getRole());

		String tokenWithPrefix = JwtUtil.BEARER_PREFIX + token.getAccessToken();
		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, tokenWithPrefix);

		String jsonResponseBody = new ObjectMapper().writeValueAsString(token);
		response.setContentType("application/json");
		response.getWriter().write(jsonResponseBody);
		response.getWriter().flush();
		response.getWriter().close();
	}

	//인증 실패(로그인 실패)시 동작하는 메서드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(400);
		authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
	}
}
