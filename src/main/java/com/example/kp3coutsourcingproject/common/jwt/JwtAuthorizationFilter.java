package com.example.kp3coutsourcingproject.common.jwt;

import com.example.kp3coutsourcingproject.common.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

	//jwtUtil과 UserDetailsServiceImpl을 의존성으로 주입
	//jwtUtil : JWT토큰 생성, 검증 등
	//UserDetailsServiceImpl : 사용자 정보 제공
	public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}


	//필터링 작업을 수행하는 메서드
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

		//JWT토큰 추출
		String tokenValue = jwtUtil.getJwtFromHeader(req);

		//토큰이 유효한지 검증
		if (StringUtils.hasText(tokenValue)) {
			if (!jwtUtil.validateToken(res, tokenValue)) {
				log.error("Token Error");
				return;
			}
			//토큰이 유효하면 사용자 정보를 추출
			Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
			try {
				//setAuthentication을 통해 인증처리(인증 객체 생성, SecurityContext생성)
				setAuthentication(info.getSubject());
			} catch (Exception e) {
				log.error(e.getMessage());
				return;
			}
		}
		//다음필터로 요청을 전달
		filterChain.doFilter(req, res);
	}

	// 인증 처리 메서드
	public void setAuthentication(String email) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = createAuthentication(email);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	// 인증 객체 생성
	private Authentication createAuthentication(String email) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
