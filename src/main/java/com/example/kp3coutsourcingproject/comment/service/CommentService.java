package com.example.kp3coutsourcingproject.comment.service;

import com.example.kp3coutsourcingproject.comment.dto.CommentRequestDto;
import com.example.kp3coutsourcingproject.comment.dto.CommentResponseDto;
import com.example.kp3coutsourcingproject.comment.repository.CommentRepository;
import com.example.kp3coutsourcingproject.post.repository.PostRepository;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	// 선택한 게시글에 대한 모든 댓글 조회(대댓글까지)
	public List<CommentResponseDto> getCommentsByPostId(Long id) {
		return null;
	}

	// 댓글 작성
	public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
		return null;
	}

	// 댓글 수정
	public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto, User user) {
		return null;
	}

	// 댓글 삭제
	public void deleteComment(Long postId, Long commentId, User user) {
	}

}
