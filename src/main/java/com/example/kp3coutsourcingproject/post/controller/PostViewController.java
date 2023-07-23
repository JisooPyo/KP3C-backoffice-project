package com.example.kp3coutsourcingproject.post.controller;

import com.example.kp3coutsourcingproject.common.file.AwsS3Service;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import com.example.kp3coutsourcingproject.post.dto.PostResponseDto;
import com.example.kp3coutsourcingproject.post.service.PostViewService;
import com.example.kp3coutsourcingproject.user.dto.UserProfileResponseDto;
import com.example.kp3coutsourcingproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/kp3c-view")
@RequiredArgsConstructor
public class PostViewController {
    private final PostViewService postViewService;
    private final UserService userService;
    private final AwsS3Service awsS3Service;

    // http://localhost:8080/kp3c-view/post
    @GetMapping("/post")
    public String showPostPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 프로필 사진, 닉네임
        UserProfileResponseDto dto = userService.getUserProfile(userDetails.getUser().getId());
        model.addAttribute("user", dto);
        return "post";
    }

    // 포스트 작성 + S3 이미지 업로드
    // http://localhost:8080/kp3c-view/post
    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createPost( @ModelAttribute PostRequestDto requestDto,
                              @RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList,
                              @AuthenticationPrincipal UserDetailsImpl userDetails, RedirectAttributes redirectAttributes) {
        if (multipartFileList.isEmpty()) {
            throw new IllegalArgumentException("파일이 유효하지 않습니다.");
        }
        // AwsS3Service의 uploadFile 메소드를 호출하여 S3에 저장
        List<String> uploadUrl = awsS3Service.uploadFile(multipartFileList);
        PostResponseDto postResponseDto = postViewService.createPost(requestDto, userDetails.getUser(), uploadUrl);

        redirectAttributes.addAttribute("id", postResponseDto.getId());

        return "redirect:/kp3c-view/post/{id}";
    }

    // 선택 포스트 조회
    // http://localhost:8080/kp3c-view/post/1
    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 프로필 사진, 닉네임
        UserProfileResponseDto profileDto = userService.getUserProfile(userDetails.getUser().getId());
        model.addAttribute("user", profileDto);
        // 포스트 조회
        PostResponseDto postDto = postViewService.getPostById(id);
        model.addAttribute("post", postDto);

        return "post-view";
    }

}
