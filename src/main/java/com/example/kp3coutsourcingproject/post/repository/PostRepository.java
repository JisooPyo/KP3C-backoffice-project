package com.example.kp3coutsourcingproject.post.repository;

import com.example.kp3coutsourcingproject.post.entity.Post;
import com.example.kp3coutsourcingproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByUser(User user);
    Optional<Post> findTopByOrderByIdDesc();
}
