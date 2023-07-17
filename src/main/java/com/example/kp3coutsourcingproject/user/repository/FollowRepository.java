package com.example.kp3coutsourcingproject.user.repository;

import com.example.kp3coutsourcingproject.user.entity.Follow;
import com.example.kp3coutsourcingproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowee(User follower, User followee);
    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);
}
