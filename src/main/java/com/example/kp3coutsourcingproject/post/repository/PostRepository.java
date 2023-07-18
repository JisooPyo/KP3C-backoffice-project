package com.example.kp3coutsourcingproject.post.repository;

import com.example.kp3coutsourcingproject.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
}
