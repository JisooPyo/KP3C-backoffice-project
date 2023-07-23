package com.example.kp3coutsourcingproject.user.controller;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.file.FileStore;
import com.example.kp3coutsourcingproject.common.jwt.JwtUtil;
import com.example.kp3coutsourcingproject.common.jwt.TokenDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.kakao.service.KakaoService;
import com.example.kp3coutsourcingproject.user.dto.SignupRequestDto;
import com.example.kp3coutsourcingproject.user.dto.UserProfileRequestDto;
import com.example.kp3coutsourcingproject.user.dto.UserProfileResponseDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/kp3c/user")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;
    private final FileStore fileStore;

    @GetMapping("/reissue")
    @ResponseBody
    public TokenDto reissueToken(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return jwtUtil.reissueToken(user.getEmail(), user.getRole());
    }

    @GetMapping("/kakao/callback")
    @ResponseBody
    public TokenDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        TokenDto tokenDto = kakaoService.kakaoLogin(code);
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, tokenDto.getAccessToken());
        cookie.setPath("/");
        response.addCookie(cookie);
        return tokenDto;
    }

    // 회원가입 프론트엔드와 연결
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SignupRequestDto requestDto, BindingResult bindingResult) throws IOException {
        // Validation 예외처리 - signupRequestDto에서 설정한 글자수, 문자규칙(a~z,A~Z,0~9)에 위배되는 경우 fieldError 리스트에 내용이 추가됨
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();

        //1건 이상 Validation 관련 에러가 발견된 경우 - 에러메시지(1개~ 여러 개)를 message응답으로 client에 전달
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                errorMessage.append(fieldError.getDefaultMessage()).append("\n ");
            }
            return "redirect:/kp3c/user/signup";
        }
        userService.signup(requestDto);
        return "signup"; // 로그인 페이지 만들면 로그인 페이지로 보내주기
    }

    // 프로필 조회
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {

        Long userId = userDetails.getUser().getId(); // userDetails - 로그인 시 정보 얻어서 사용
        UserProfileResponseDto dto = userService.getUserProfile(userId); // 뷰에 보여줄 데이터

        model.addAttribute("user", dto); // user 라는 이름에 db에 저장된 데이터 뿌려주기

        return "profile";
    }
    // 프로필 수정
    @PostMapping("/profile")
    public String modifyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute UserProfileRequestDto requestDto) throws IOException {

        Long userId = userDetails.getUser().getId();
        userService.updateUserProfile(userId, requestDto);

        return "redirect:/kp3c/user/profile"; // 수정 후 조회 화면에서 데이터 보여주기
    }

    // 저장된 경로에서 이미지 다운로드 하여 보여주기
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @ResponseBody
    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponseDto> logout(HttpServletResponse res, @RequestBody TokenDto tokenDto) throws IOException {
        userService.logout(res, tokenDto.getAccessToken());
        return ResponseEntity.ok().body(new ApiResponseDto("로그아웃 성공", HttpStatus.OK.value()));
    }
}
