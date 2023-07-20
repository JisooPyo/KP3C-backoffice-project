package com.example.kp3coutsourcingproject.timeline.service;

import com.example.kp3coutsourcingproject.post.entity.Post;
import com.example.kp3coutsourcingproject.post.repository.PostRepository;
import com.example.kp3coutsourcingproject.timeline.dto.FeedPostDto;
import com.example.kp3coutsourcingproject.timeline.dto.FeedResponseDto;
import com.example.kp3coutsourcingproject.timeline.repository.FeedRepository;
import com.example.kp3coutsourcingproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {
    private final RedisTemplate<String, FeedPostDto> redisTemplate;
    private final PostRepository postRepository;
    private final FeedRepository feedRepository;

    // 가장 최근에 작성한 게시글 저장하기
    public void saveMyLastCreatedPost(User user) {
        ListOperations<String, FeedPostDto> listOperations = redisTemplate.opsForList();
        // id가 가장 마지막인 post를 찾는다
        Post lastCreatedPost = postRepository.findTopByOrderByIdDesc().orElseThrow( () ->
                new IllegalArgumentException("작성한 게시글이 없습니다.")
        );

        // redis에 저장
        String key = user.getId().toString();
        FeedPostDto value = new FeedPostDto(lastCreatedPost);
        listOperations.leftPush(key, value); // 저장
    }
    
    // 내 최근 게시글 조회
    public List<FeedPostDto> getMyLastCreatedPosts(User user) {
        ListOperations<String, FeedPostDto> listOperations = redisTemplate.opsForList();

        String key = user.getId().toString();
        long size = listOperations.size(key) == null ? 0 : listOperations.size(key);

        return listOperations.range(key, 0, size);
    }

    // 내가 팔로우 한 사람의 게시글 저장하기
    public void saveMyFollweePost() {

    }

    // 내 피드 조회
    public FeedResponseDto getMyFeed(User user) {
        return null;
    }
}

