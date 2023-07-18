package com.example.kp3coutsourcingproject.user.controller;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.jwt.JwtUtil;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.sociallogin.service.KakaoService;
import com.example.kp3coutsourcingproject.user.dto.ProfileDto;
import com.example.kp3coutsourcingproject.user.dto.SignupRequestDto;
import com.example.kp3coutsourcingproject.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/kp3c/user")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult, HttpServletResponse response) {
        // Validation 예외처리 - signupRequestDto에서 설정한 글자수, 문자규칙(a~z,A~Z,0~9)에 위배되는 경우 fieldError 리스트에 내용이 추가됨
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();

        //1건 이상 Validation 관련 에러가 발견된 경우 - 에러메시지(1개~ 여러 개)를 message응답으로 client에 전달
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                errorMessage.append(fieldError.getDefaultMessage()).append("\n ");
            }
            response.setStatus(400);
            return ResponseEntity.status(400).body(new ApiResponseDto(errorMessage.toString(), response.getStatus()));
        }

        return ResponseEntity.status(response.getStatus()).body(userService.signup(requestDto, response));
    }

    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        // code: 카카오 서버로부터 받은 인가코드 service 전달 후 인증처리 및 JWT반환
        String token = kakaoService.kakaoLogin(code);

        // cookie 생성 및 직접 브라우저에 set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<ProfileDto>> getFollowers(@PathVariable String username) {
        List<ProfileDto> followers = userService.getFollowers(username);
        return ResponseEntity.ok().body(followers);
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<List<ProfileDto>> getFollowing(@PathVariable String username) {
        List<ProfileDto> following = userService.getFollowing(username);
        return ResponseEntity.ok().body(following);
    }

    @PostMapping("/follow/{username}")
    public ResponseEntity<ApiResponseDto> follow(@PathVariable String username, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.follow(username, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("팔로우 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/unfollow/{username}")
    public ResponseEntity<ApiResponseDto> unfollow(@PathVariable String username, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.unfollow(username, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("언팔로우 성공", HttpStatus.OK.value()));
    }
}
