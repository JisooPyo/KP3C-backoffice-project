package com.example.kp3coutsourcingproject.comment.controller;

import com.example.kp3coutsourcingproject.comment.dto.CommentRequestDto;
import com.example.kp3coutsourcingproject.comment.dto.CommentResponseDto;
import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kp3c/post")
public class CommentController {
	// 포스트의 댓글 전체 조회
	@GetMapping("/{postId}/comments")
	public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
		return null;
	}

	// 선택 댓글 조회
	@GetMapping("/{postId}/comment/{id}")
	public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long postId,
														 @PathVariable Long id){
		return null;
	}

	// 댓글 작성
	@PostMapping("/{postId}/comment")
	public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId,
															@RequestBody CommentRequestDto requestDto,
															@AuthenticationPrincipal UserDetailsImpl userDetails){
		return null;
	}

	// 댓글 수정
	@PutMapping("/{postId}/comment/{id}")
	public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long postId,
															@PathVariable Long id,
															@RequestBody CommentRequestDto requestDto,
															@AuthenticationPrincipal UserDetailsImpl userDetails){
		return null;
	}

	@DeleteMapping("/{postId}/comment/{id}")
	public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long postId,
														@PathVariable Long id,
														@AuthenticationPrincipal UserDetailsImpl userDetails){
		return null;
	}


	// 댓글 삭제
	/{postId}/comment/{commentId}
}
