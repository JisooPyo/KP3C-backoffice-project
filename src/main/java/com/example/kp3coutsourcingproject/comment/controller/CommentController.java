package com.example.kp3coutsourcingproject.comment.controller;

import com.example.kp3coutsourcingproject.comment.dto.CommentRequestDto;
import com.example.kp3coutsourcingproject.comment.dto.CommentResponseDto;
import com.example.kp3coutsourcingproject.comment.service.CommentService;
import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.example.kp3coutsourcingproject.common.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kp3c/post")
public class CommentController {
	private final CommentService commentService;

	// 선택한 게시글에 대한 모든 댓글 조회(대댓글까지)
	@GetMapping("/{id}/comments")
	public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long id) {
		List<CommentResponseDto> results = commentService.getCommentsByPostId(id);
		return ResponseEntity.ok().body(results);
	}

	// 댓글 작성
	@PostMapping("{id}/comment")	// id로 바꾸기
	public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long id,
															@RequestBody CommentRequestDto requestDto,
															@AuthenticationPrincipal UserDetailsImpl userDetails) {
		CommentResponseDto result = commentService.createComment(id, requestDto, userDetails.getUser());
		return ResponseEntity.ok().body(result);
	}

	// 댓글 수정
	@PutMapping("{postId}/comment/{commentId}")
	public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long postId,
															@PathVariable Long commentId,
															@RequestBody CommentRequestDto requestDto,
															@AuthenticationPrincipal UserDetailsImpl userDetails) {
		CommentResponseDto result = commentService.updateComment(postId, commentId, requestDto, userDetails.getUser());
		return ResponseEntity.ok().body(result);
	}

	// 댓글 삭제
	@DeleteMapping("{postId}/comment/{commentId}")
	public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long postId,
														@PathVariable Long commentId,
														@AuthenticationPrincipal UserDetailsImpl userDetails) {
		commentService.deleteComment(postId, commentId, userDetails.getUser());
		return ResponseEntity.ok().body(new ApiResponseDto("해당 댓글 삭제 완료", HttpStatus.OK.value()));
	}
}
