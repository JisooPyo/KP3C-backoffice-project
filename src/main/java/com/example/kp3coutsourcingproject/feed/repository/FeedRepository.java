package com.example.kp3coutsourcingproject.feed.repository;

import com.example.kp3coutsourcingproject.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
}
