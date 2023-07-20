package com.example.kp3coutsourcingproject.common.security;

import com.example.kp3coutsourcingproject.common.jwt.JwtAuthenticationFilter;
import com.example.kp3coutsourcingproject.common.jwt.JwtAuthorizationFilter;
import com.example.kp3coutsourcingproject.common.jwt.JwtUtil;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // 메서드에 @secured를 가능하게 함.(인가)
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;
	private final AuthenticationConfiguration authenticationConfiguration;

	// AuthenticationManager Bean 등록 ( AuthenticationConfiguration을 통해 만든다. )
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	// Jwt로 로그인 인증하는 필터 Bean 등록
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
		filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
		return filter;
	}

	// JWT 인가 처리 수행
	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter() {
		return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
	}

	// 지금까지는 filter를 만들기만 한 것. SecurityFilterChain에 끼워줘야 한다.
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// CSRF 설정
		http.csrf((csrf) -> csrf.disable());

		// 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
		http.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);

		http.authorizeHttpRequests((authorizeHttpRequests) ->
				authorizeHttpRequests
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
						.requestMatchers("/kp3c/user/**").permitAll() // '/api/user/'로 시작하는 요청 모두 접근 허가
						.requestMatchers("/kp3c/manage/**").hasRole("ADMIN") // 관리자="ADMIN"만 /manage 도메인 접근 가능
						.requestMatchers("/redis/**").permitAll() // redis용 임시 허가
						// sns는 보통 로그인 안하면 아무것도 못하니까 일단은 요거만 해놓고
						// 만약 다른 요청도 접근 허가가 되어야 할 것 같다고 하면 추가 하겠습니다.
						.anyRequest().authenticated() // 그 외 모든 요청 인증처리
		);

		// login 페이지 이용안함.
		http.formLogin((formLogin) ->
				formLogin.disable()
		);

		// 필터 관리
		http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
