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

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	// 선택한 게시글에 대한 모든 댓글 조회(대댓글까지)
	public List<CommentResponseDto> getAllCommentsByPostId(Long id) {
		Post post = findPost(id);
		List<Comment> commentList = commentRepository.findAllByPost(post);

		return convertNestedStructure(commentList);

	}

	// 선택한 게시글에 대한 댓글 조회
	public List<CommentResponseDto> getCommentsByPostId(Long id) {

		return null;
	}

	// 댓글 작성
	public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
		User targetUser = findUser(user.getId());
		Post post = findPost(postId);
		Comment parent = null;
		// 자식댓글인 경우
		if (requestDto.getParentId() != null) {
			parent = findComment(requestDto.getParentId());
			// 부모댓글의 post id와 자식댓글의 post id가 같은지 확인
			if (!(parent.getPost().getId().equals(postId))){
				throw new IllegalArgumentException("잘못된 접근입니다.");
			}
		}

		Comment comment = Comment.builder()
				.post(post)
				.content(requestDto.getContent())
				.user(targetUser)
				.build();

		// 자식댓글인 경우
		if(parent != null){
			comment.updateParent(parent);
		}
		commentRepository.save(comment);
		CommentResponseDto responseDto = null;

		// 자식댓글인 경우
		if(parent != null){
			responseDto = new CommentResponseDto(comment);
		} else {
			responseDto = CommentResponseDto.builder()
					.id(comment.getId())
					.nickname(comment.getUser().getNickname())
					.username(comment.getUser().getUsername())
					.createdAt(comment.getCreatedAt())
					.content(comment.getContent())
					.build();
		}

		return responseDto;

	}

	// 댓글 수정
	public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto, User user) {
		return null;
	}

	// 댓글 삭제
	public void deleteComment(Long postId, Long commentId, User user) {
	}

	// userId로 User 찾기
	private User findUser(Long id) {
		return userRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("선택한 유저는 존재하지 않습니다.")
		);
	}

	// postId로 Post 찾기
	private Post findPost(Long id) {
		return postRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("선택한 포스트는 존재하지 않습니다.")
		);
	}

	// commentId로 Comment 찾기
	private Comment findComment(Long id) {
		return commentRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("선택한 댓글은 존재하지 않습니다.")
		);
	}

	// List<Comment>를 중첩구조로 변환하는 코드
	private List<CommentResponseDto> convertNestedStructure(List<Comment>commentList){
		List<CommentResponseDto> responseDtoList = new ArrayList<>();
		Map<Long, CommentResponseDto> map = new HashMap<>();
		commentList.stream().forEach(c->{
			CommentResponseDto responseDto = new CommentResponseDto(c);
			map.put(responseDto.getId(),responseDto);
			if(c.getParent()!=null){
				map.get(c.getParent().getId()).getChildren().add(responseDto);
			} else {
				responseDtoList.add(responseDto);
			}
		});
		return responseDtoList;
	}


}
