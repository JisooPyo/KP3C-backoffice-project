package com.example.kp3coutsourcingproject.common.jwt;

import com.example.kp3coutsourcingproject.common.exception.CustomException;
import com.example.kp3coutsourcingproject.common.exception.ErrorCode;
import com.example.kp3coutsourcingproject.common.redis.RedisUtils;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Slf4j(topic = "Jwt 관련 로그")
@Component
public class JwtUtil {
	private final RedisUtils redisUtils;
	public static final String AUTHORIZATION_HEADER = "Authorization"; // Header KEY 값
	public static final String AUTHORIZATION_KEY = "auth"; // 사용자 권한 값의 KEY
	public static final String BEARER_PREFIX = "Bearer "; // Token 식별자
	private final long ACCESS_TOKEN_TIME = 1000L * 60 * 30; // 30분
	private final long REFRESH_TOKEN_TIME = 1000L * 60 * 60 * 24 * 3; // 3일

	@Value("${jwt.secret.key}") // application.properties에 명시되어 있는 secretKey
	private String secretKey;
	private Key key; // Decode된 Secret Key를 담는 객체
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 알고리즘

	public JwtUtil(RedisUtils redisUtils) {
		this.redisUtils = redisUtils;
	}

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public TokenResponse issueToken(String email, UserRoleEnum role) {
		String accessToken = createAccessToken(email, role);
		String refreshToken = createRefreshToken();
		redisUtils.put(email, refreshToken, REFRESH_TOKEN_TIME);
		return new TokenResponse(accessToken, refreshToken);
	}

	public TokenResponse reissueToken(String email, UserRoleEnum role) {
		String refreshToken = redisUtils.get(email, String.class);
		if(Objects.isNull(refreshToken)) {
			throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
		}
		String accessToken = createAccessToken(email, role);
		return new TokenResponse(accessToken, null);
	}

	private String createAccessToken(String email, UserRoleEnum role) {
		Date now = new Date();
		Date expireDate = new Date(now.getTime() + ACCESS_TOKEN_TIME);

		return Jwts.builder()
				.setSubject(email)
				.claim(AUTHORIZATION_KEY, role)
				.setIssuedAt(now)
				.setExpiration(expireDate)
				.signWith(key, signatureAlgorithm)
				.compact();
	}

	private String createRefreshToken() {
		Date now = new Date();
		Date expireDate = new Date(now.getTime() + REFRESH_TOKEN_TIME);

		return Jwts.builder()
				.setIssuedAt(now)
				.setExpiration(expireDate)
				.signWith(key, signatureAlgorithm)
				.compact();
	}

	// header 에서 JWT 가져오기
	public String getJwtFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}

	// 토큰 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch(ExpiredJwtException e) {
			log.error(ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage());
			throw new CustomException(ErrorCode.EXPIRED_ACCESS_TOKEN);
		} catch(JwtException e) {
			log.error(ErrorCode.INVALID_TOKEN.getMessage());
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
	}

	// 토큰에서 사용자 정보 가져오기
	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}
