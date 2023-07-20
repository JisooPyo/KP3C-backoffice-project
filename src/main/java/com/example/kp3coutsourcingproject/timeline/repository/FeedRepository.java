package com.example.kp3coutsourcingproject.timeline.repository;

import com.example.kp3coutsourcingproject.timeline.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
}
