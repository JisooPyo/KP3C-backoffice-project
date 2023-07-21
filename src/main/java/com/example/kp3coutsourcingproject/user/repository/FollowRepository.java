package com.example.kp3coutsourcingproject.user.repository;

import com.example.kp3coutsourcingproject.user.entity.Follow;
import com.example.kp3coutsourcingproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowee(User follower, User followee);
    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);
    List<Follow> findAllByFollowee(User followee);
    List<Follow> findAllByFollower(User follower);
}
