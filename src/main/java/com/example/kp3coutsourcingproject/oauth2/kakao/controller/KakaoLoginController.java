package com.example.kp3coutsourcingproject.oauth2.kakao.controller;

import com.example.kp3coutsourcingproject.common.jwt.JwtUtil;
import com.example.kp3coutsourcingproject.oauth2.kakao.service.KakaoLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class KakaoLoginController {
    KakaoLoginService kakaoService;

        @GetMapping("/kakao/callback")
        public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

            // code: 카카오 서버로부터 받은 인가코드 service 전달 후 인증처리 및 JWT반환
            String token = kakaoService.kakaoLogin(code);

            // cookie 생성 및 직접 브라우저에 set
            Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/";
        }
}
