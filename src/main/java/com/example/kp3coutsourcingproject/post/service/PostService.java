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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	// 모든 글 계층형으로 조회(답글까지)
	public List<PostResponseDto> getAllPosts() {
		List<Post> postList = postRepository.findAllPosts();
		return convertNestedStructure(postList);
	}

	// 홈피드(유저 작성 글 + 유저가 팔로잉한 글)
	public List<PostResponseDto> getHomeFeed(User user) {
		List<PostResponseDto> posts = postRepository.getHomeFeed(user.getId()).stream()
				.map(PostResponseDto::new)
				.collect(Collectors.toList());

		return posts;
	}

	// 자기피드(유저 작성 글만)
	public List<PostResponseDto> getMyFeed(User user) {
		List<PostResponseDto> posts = postRepository.getUserFeed(user.getId()).stream()
				.map(PostResponseDto::new)
				.collect(Collectors.toList());

		return posts;
	}

	// 선택한 게시글에 대한 모든 답글 조회(답글의 답글 x, 답글만!)
	public List<PostResponseDto> getChildPosts(Long id) {
		List<PostResponseDto> childPosts = postRepository.findAllByParent(findPost(id)).stream()
				.map(PostResponseDto::new)
				.collect(Collectors.toList());
		return childPosts;
	}

	//////////////////////////////////////////////////////////////////////////

	// 포스트 작성
	public PostResponseDto createPost(PostRequestDto requestDto, User user) {
		User targetUser = findUser(user.getId());

		Post parent = null;
		// 자식글인 경우
		if(requestDto.getParentId()!=null){
			parent = findPost(requestDto.getParentId());
		}

		Post post = Post.builder()
				.content(requestDto.getContent())
				.user(targetUser)
				.build();

		// 자식글인 경우
		if(parent != null){
			post.updateParent(parent);
		}

		postRepository.save(post);
		PostResponseDto responseDto = new PostResponseDto(post);

		return responseDto;
	}

	// 포스트 수정
	@Transactional
	public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
		Post post = findPost(id);
		User targetUser = findUser(user.getId());
		// 게시글 작성자인지 체크
		if (isPostAuthor(post, targetUser)) {
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
		if (isPostAuthor(post, targetUser)) {
			postRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("게시글 작성자만이 게시글을 삭제할 수 있습니다.");
		}
	}

	/////////////////////////////////////////////////////////

	// id값으로 부모 post 찾기
	private Post findParent(long id) {
		return postRepository.findById(id).orElse(null);
	}

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
	private boolean isPostAuthor(Post post, User user) {
		if (post.getUser().getId().equals(user.getId())) {
			return true;
		} else {
			return false;
		}
	}

	// List<Post>를 responseDto로 변환해 주면서 중첩구조로 변환하는 코드
	private List<PostResponseDto> convertNestedStructure (List<Post> postList){
		List<PostResponseDto> responseDtoList = new ArrayList<>();
		Map<Long,PostResponseDto> map = new HashMap<>();
		postList.stream().forEach(c -> {
			PostResponseDto responseDto = new PostResponseDto(c);
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
