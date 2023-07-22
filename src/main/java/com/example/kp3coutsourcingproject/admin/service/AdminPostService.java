package com.example.kp3coutsourcingproject.admin.service;

import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import com.example.kp3coutsourcingproject.post.dto.PostResponseDto;
import com.example.kp3coutsourcingproject.post.entity.Post;
import com.example.kp3coutsourcingproject.post.repository.PostRepository;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminPostService {

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        Post post = new Post(requestDto);
        post.setUser(admin);
        postRepository.save(post);

        return new PostResponseDto(post);
    }

    public Page<PostResponseDto> getPosts(User admin, int page, int size, String sortBy, boolean isAsc) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }
        // 페이징 기능
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 유저 전체 찾아서 responseDto로 반환
        Page<Post> postList = postRepository.findAll(pageable);
        return postList.map(PostResponseDto::new);
    }

    public PostResponseDto getPost(Long postId, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        Post findPost = findPost(postId);
        return new PostResponseDto(findPost);
    }


    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        Post findPost = findPost(postId);
        findPost.update(requestDto);

        return new PostResponseDto(findPost);
    }

    public void deletePost(Long postId, User admin) {
        // 회원 권한 확인
        if (!isAdmin(admin)) {
            throw new IllegalArgumentException("관리자 권한이 있어야만 해당 요청을 실행할 수 있습니다.");
        }

        Post findPost = findPost(postId);
        postRepository.delete(findPost);
    }

    private boolean isAdmin(User admin) {
        UserRoleEnum userRoleEnum = admin.getRole();
        if (userRoleEnum != UserRoleEnum.ADMIN) return false;
        return true;
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글은 존재하지 않습니다.")
        );
    }

}
