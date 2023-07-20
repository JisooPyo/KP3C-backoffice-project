package com.example.kp3coutsourcingproject.post.repository;

import com.example.kp3coutsourcingproject.post.entity.Post;

import java.util.List;

public interface PostCustomRepository {
	List<Post> getHomeFeed(Long userId);

	List<Post> getUserFeed(Long userId);

}
