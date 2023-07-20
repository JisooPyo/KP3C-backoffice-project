package com.example.kp3coutsourcingproject.comment.repository;

import com.example.kp3coutsourcingproject.comment.entity.Comment;

import java.util.List;

public interface CommentCustomRepository {
	List<Comment> getComments(Long postId);
}
