package com.example.kp3coutsourcingproject.common.jwt;

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

@Slf4j(topic = "Jwt 관련 로그")
@Component
public class JwtUtil {

	public static final String AUTHORIZATION_HEADER = "Authorization"; // Header KEY 값
	public static final String AUTHORIZATION_KEY = "auth"; // 사용자 권한 값의 KEY
	public static final String BEARER_PREFIX = "Bearer "; // Token 식별자
	private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

	@Value("${jwt.secret.key}") // application.properties에 명시되어 있는 secretKey
	private String secretKey;
	private Key key; // Decode된 Secret Key를 담는 객체
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 알고리즘

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	// JWT 생성
	public String createToken(String username, UserRoleEnum role) {
		Date date = new Date();

		return BEARER_PREFIX +
				Jwts.builder()
						.setSubject(username) // 사용자 식별자값(ID)
						.claim(AUTHORIZATION_KEY, role) // 사용자 권한
						.setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
						.setIssuedAt(date) // 발급일
						.signWith(key, signatureAlgorithm) // 암호화 알고리즘
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
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
		return false;
	}

	// 토큰에서 사용자 정보 가져오기
	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	/*소셜 로그인 때문에 넣은 것들*/

}
