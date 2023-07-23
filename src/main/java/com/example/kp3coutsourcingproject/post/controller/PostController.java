package com.example.kp3coutsourcingproject.post.controller;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import com.example.kp3coutsourcingproject.post.dto.PostResponseDto;
import com.example.kp3coutsourcingproject.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kp3c")
@RequiredArgsConstructor
@PreAuthorize("principal.enabled")
public class PostController {
	private final PostService postService;

	// 모든 글 계층형으로 조회(답글까지) - 어디에서 쓰이겠지 뭐..
	@GetMapping("/allPosts")
	public ResponseEntity<List<PostResponseDto>> getAllPosts(){
		List<PostResponseDto> results = postService.getAllPosts();
		return ResponseEntity.ok().body(results);
	}

	//////////////////////////////////////////////////////////////////////////////////

	// 홈피드(유저 작성 글 + 유저가 팔로잉한 글)
	// http://localhost:8080/kp3c/posts
	@GetMapping("/homeFeed")
	public ResponseEntity<List<PostResponseDto>> getHomeFeed(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<PostResponseDto> results = postService.getHomeFeed(userDetails.getUser());

		return ResponseEntity.ok().body(results);
	}

	// 자기피드(유저 작성 글만)
	// http://localhost:8080/kp3c/post/1
	@GetMapping("/myFeed")
	public ResponseEntity<List<PostResponseDto>> getMyFeed(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		List<PostResponseDto> results = postService.getMyFeed(userDetails.getUser());
		return ResponseEntity.ok().body(results);
	}

	// 선택한 게시글에 대한 모든 답글 조회(답글의 답글 x, 답글만!)
	@GetMapping("/post/{id}")
	public ResponseEntity<List<PostResponseDto>> getChildPosts(@PathVariable Long id){
		List<PostResponseDto> results = postService.getChildPosts(id);
		return ResponseEntity.ok().body(results);
	}

	/////////////////////////////////////////////////////////////////////////////////

	// 포스트 작성
	// http://localhost:8080/kp3c/post
	@PostMapping("/post")
	public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto requestDto,
													  @AuthenticationPrincipal UserDetailsImpl userDetails) {
		PostResponseDto result = postService.createPost(requestDto, userDetails.getUser());
		// 201 : CREATED
		return ResponseEntity.status(201).body(result);
	}

	// 포스트 수정
	// http://localhost:8080/kp3c/post/1
	@PutMapping("/post/{id}")
	public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id,
													  @Valid @RequestBody PostRequestDto requestDto,
													  @AuthenticationPrincipal UserDetailsImpl userDetails) {
		PostResponseDto result = postService.updatePost(id, requestDto, userDetails.getUser());
		return ResponseEntity.ok().body(result);
	}

	// 포스트 삭제
	// http://localhost:8080/kp3c/post/1
	@DeleteMapping("/post/{id}")
	public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id,
													 @AuthenticationPrincipal UserDetailsImpl userDetails) {
		postService.deletePost(id, userDetails.getUser());
		return ResponseEntity.ok().body(
				new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value())
		);
	}
}
