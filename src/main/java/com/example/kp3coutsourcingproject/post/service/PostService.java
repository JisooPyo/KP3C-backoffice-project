package com.example.kp3coutsourcingproject.post.service;

import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import com.example.kp3coutsourcingproject.post.dto.PostResponseDto;
import com.example.kp3coutsourcingproject.post.entity.Post;
import com.example.kp3coutsourcingproject.post.repository.PostRepository;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	// 전체 포스트 조회
	public List<PostResponseDto> getPosts() {
		List<PostResponseDto> posts = postRepository.findAll().stream()
				.map(PostResponseDto::new)
				.collect(Collectors.toList());

		return posts;
	}

	// 선택 포스트 조회
	public PostResponseDto getPostById(Long id) {
		Post post = findPost(id);
		return new PostResponseDto(post);
	}

	// 포스트 작성
	public PostResponseDto createPost(PostRequestDto requestDto, User user) {
		User targetUser = findUser(user.getId());
		Post post = new Post(requestDto);
		post.setUser(targetUser);
		postRepository.save(post);
		return new PostResponseDto(post);
	}

	// 포스트 수정
	@Transactional
	public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
		Post post = findPost(id);
		User targetUser = findUser(user.getId());
		// 게시글 작성자인지 체크
		if(isPostAuthor(post,targetUser)){
			post.setContent(requestDto.getContent());
			return new PostResponseDto(post);
		} else {
			throw new IllegalArgumentException("게시글 작성자만이 게시글을 수정할 수 있습니다.");
		}
	}

	// 포스트 삭제
	public void deletePost(Long id, User user) {
		Post post = findPost(id);
		User targetUser = findUser(user.getId());
		if(isPostAuthor(post,targetUser)){
			postRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("게시글 작성자만이 게시글을 삭제할 수 있습니다.");
		}
	}

	/////////////////////////////////////////////////////////

	// id값으로 post 찾기
	private Post findPost(long id) {
		return postRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("존재하지 않는 게시글입니다.")
		);
	}

	// id값으로 user 찾기
	private User findUser(long id) {
		return userRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("존재하지 않는 유저입니다.")
		);
	}

	// 게시글의 작성자가 유저인지 확인
	private boolean isPostAuthor(Post post, User user){
		if(post.getUser().getId().equals(user.getId())){
			return true;
		} else {
			return false;
		}
	}


}
