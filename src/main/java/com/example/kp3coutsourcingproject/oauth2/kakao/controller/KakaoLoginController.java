package com.example.kp3coutsourcingproject.oauth2.kakao.controller;

import com.example.kp3coutsourcingproject.oauth2.kakao.service.KakaoLoginService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/kp3c/user", produces = "application/json")
public class KakaoLoginController {
    KakaoLoginService kakaoService;

    public KakaoLoginController(KakaoLoginService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao/{registrationId}")
    public void kakaologin(@RequestParam String code, @PathVariable String registrationId) {
        kakaoService.socialLogin(code, registrationId);
    }

//        @GetMapping("/kakao/callback")
//        public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
//
//            // code: 카카오 서버로부터 받은 인가코드 service 전달 후 인증처리 및 JWT반환
//            String token = kakaoService.kakaoLogin(code);
//
//            // cookie 생성 및 직접 브라우저에 set
//            Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
//            cookie.setPath("/");
//            response.addCookie(cookie);
//
//            return "redirect:/";
//        }
}
