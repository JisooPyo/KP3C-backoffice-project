package com.example.kp3coutsourcingproject.comment.service;

import com.example.kp3coutsourcingproject.comment.dto.CommentRequestDto;
import com.example.kp3coutsourcingproject.comment.dto.CommentResponseDto;
import com.example.kp3coutsourcingproject.comment.entity.Comment;
import com.example.kp3coutsourcingproject.comment.repository.CommentRepository;
import com.example.kp3coutsourcingproject.post.entity.Post;
import com.example.kp3coutsourcingproject.post.repository.PostRepository;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	// 포스트의 댓글 전체 조회

	// 선택 댓글 조회

	// 댓글 작성
	public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
		User targetUser = findUser(user.getId());
		Post post = findPost(postId);
		Comment parent = findComment(requestDto.getParentId());
		String content = requestDto.getContent();
		Comment comment = new Comment(targetUser, post, parent, content);
		commentRepository.save(comment);
		return new CommentResponseDto(comment);
	}

	// 댓글 수정

	// 댓글 삭제

	///////////////////////////////////////////////////////////

	private User findUser(Long id) {
		return userRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("존재하지 않는 유저입니다.")
		);
	}

	private Post findPost(Long id) {
		return postRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("존재하지 않는 게시글입니다.")
		);
	}

	private Comment findComment(Long id) {
		return commentRepository.findById(id).orElse(null);
	}
}
