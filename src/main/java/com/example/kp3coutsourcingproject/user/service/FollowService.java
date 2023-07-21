package com.example.kp3coutsourcingproject.user.service;

import com.example.kp3coutsourcingproject.user.dto.FollowUserDto;
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

    public List<FollowUserDto> getFollowers(String username) {
        User user = findUser(username);
        List<Follow> followerList = followRepository.findAllByFollowee(user);
        return followerList
                .stream()
                .map(Follow::getFollower)
                .map(FollowUserDto::new)
                .toList();
    }

    public List<FollowUserDto> getFollowing(String username) {
        User user = findUser(username);
        List<Follow> followingList = followRepository.findAllByFollower(user);
        return followingList
                .stream()
                .map(Follow::getFollowee)
                .map(FollowUserDto::new)
                .toList();
    }

    public void follow(String username, User user) {
        User followee = findUser(username);
        if (followee.getId().equals(user.getId())) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }
        if (followRepository.existsByFollowerAndFollowee(user, followee)) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }
        updateFollowCount(user, followee, +1);
        userRepository.save(user);
        followRepository.save(new Follow(user, followee));
    }

    public void unfollow(String username, User user) {
        User followee = findUser(username);
        if (followee.getId().equals(user.getId())) {
            throw new IllegalArgumentException("자기 자신을 언팔로우할 수 없습니다.");
        }
        Follow existedFollow = followRepository.findByFollowerAndFollowee(user, followee).orElseThrow(() ->
                new IllegalArgumentException("팔로우하지 않은 사용자입니다.")
        );
        followRepository.delete(existedFollow);
        updateFollowCount(user, followee, -1);
        userRepository.save(user);
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

    public void updateFollowCount(User follower, User followee, Integer value) {
        follower.updateFollowingCount(value);
        followee.updateFollowerCount(value);
    }
}
