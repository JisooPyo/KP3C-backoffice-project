package com.example.kp3coutsourcingproject.sociallogin.service;

import com.example.kp3coutsourcingproject.jwt.JwtUtil;
import com.example.kp3coutsourcingproject.sociallogin.dto.KakaoUserInfoDto;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    // 주입받을 것들
//    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public String kakaoLogin(String code) throws JsonProcessingException{
        //1. 인가 코드로 액세스 토큰 요청
        String accessToken = getToken(code);
        //2. 토큰으로 카카오api호출 : "액세스토큰"으로 "카카오 사용자 정보"가저오기

        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        return null;
    }
}
