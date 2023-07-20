package com.example.kp3coutsourcingproject.redis.service;

import com.example.kp3coutsourcingproject.post.dto.PostResponseDto;
import com.example.kp3coutsourcingproject.post.entity.Post;
import com.example.kp3coutsourcingproject.post.repository.PostRepository;
import com.example.kp3coutsourcingproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisTimelineService {
    private final RedisTemplate<String, String> redisTemplate;
    private final PostRepository postRepository;

    // 가장 최근에 작성한 게시글 저장하기
    public void saveMyLastCreatedPost(User user) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        // id가 가장 마지막인 post를 찾는다
        Post lastCreatedPost = postRepository.findTopByOrderByIdDesc().orElseThrow( () ->
                new IllegalArgumentException("작성한 게시글이 없습니다.")
        );

        String key = user.getId().toString();
        String value = lastCreatedPost.getId().toString();
        listOperations.leftPush(key, value); // 저장
        //
        // Date.from(ZonedDateTime.now().plusDays(14).toInstant()); // 유효기간 TTL 2주 설정?
    }
    
    // 타임라인 게시글 조회
    public List<PostResponseDto> getTimelinePosts(User user) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();

        String key = user.getId().toString();
        long size = listOperations.size(key) == null ? 0 : listOperations.size(key);

        List<Post> posts = new LinkedList<>();
        for (String postId : listOperations.range(key, 0, size)) {
            Long id = Long.parseLong(postId); // id 파싱
            posts.add(postRepository.findById(id).orElseThrow( () ->
                    new IllegalArgumentException("게시글이 존재하지 않습니다."))
            );
        }

        return posts.stream().map(PostResponseDto::new).toList();
    }
}

