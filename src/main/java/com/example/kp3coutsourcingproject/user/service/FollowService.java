package com.example.kp3coutsourcingproject.user.service;

import com.example.kp3coutsourcingproject.user.dto.ProfileDto;
import com.example.kp3coutsourcingproject.user.entity.Follow;
import com.example.kp3coutsourcingproject.user.entity.User;
import com.example.kp3coutsourcingproject.user.repository.FollowRepository;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public List<ProfileDto> getFollowers(String username) {
        User user = findUser(username);
        return user.getFollowList()
                .stream()
                .map(Follow::getFollower)
                .map(ProfileDto::new)
                .toList();
    }

    public List<ProfileDto> getFollowing(String username) {
        User user = findUser(username);
        return user.getFollowingList()
                .stream()
                .map(Follow::getFollowee)
                .map(ProfileDto::new)
                .toList();
    }

    public void follow(String username, User user) {
        User followee = findUser(username);
        if(followee.getId().equals(user.getId())) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }
        if(followRepository.existsByFollowerAndFollowee(user, followee)) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }
        followRepository.save(new Follow(user, followee));
    }

    public void unfollow(String username, User user) {
        User followee = findUser(username);
        if(followee.getId().equals(user.getId())) {
            throw new IllegalArgumentException("자기 자신을 언팔로우할 수 없습니다.");
        }
        Follow existedFollow = followRepository.findByFollowerAndFollowee(user, followee).orElseThrow(() ->
                new IllegalArgumentException("팔로우하지 않은 사용자입니다.")
        );
        followRepository.delete(existedFollow);
    }

    public boolean isFollowing(String username, User user) {
        User followee = findUser(username);
        return followRepository.existsByFollowerAndFollowee(user, followee);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
    }
}
