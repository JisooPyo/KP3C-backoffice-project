package com.example.kp3coutsourcingproject.comment.repository;

import com.example.kp3coutsourcingproject.comment.entity.Comment;
import com.example.kp3coutsourcingproject.post.entity.Post;

import java.util.List;

public interface CommentCustomRepository {
	List<Comment> findAllByPost(Post post);
}
