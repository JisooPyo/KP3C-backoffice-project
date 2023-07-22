package com.example.kp3coutsourcingproject.post.service;

import com.example.kp3coutsourcingproject.common.file.Post_Image;
import com.example.kp3coutsourcingproject.common.file.ImageRepository;
import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import com.example.kp3coutsourcingproject.post.dto.PostResponseDto;
import com.example.kp3coutsourcingproject.post.entity.Post;
import com.example.kp3coutsourcingproject.post.repository.PostRepository;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostViewService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final ImageRepository imageRepository;

	// 선택 포스트 조회
	public PostResponseDto getPostById(Long id) {
		Post post = findPost(id);

		imageRepository.findByPostId(post.getId());
		return new PostResponseDto(post);
	}

	// 포스트 작성
	public PostResponseDto createPost(PostRequestDto requestDto, User user, List<String> imgPaths) {
		User targetUser = findUser(user.getId());
		Post post = new Post(requestDto);
		post.setUser(targetUser);
		postRepository.save(post);

		for (String imgUrl : imgPaths) {
			Post_Image image= new Post_Image(imgUrl, post);
			imageRepository.save(image);
		}
		return new PostResponseDto(post);
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
