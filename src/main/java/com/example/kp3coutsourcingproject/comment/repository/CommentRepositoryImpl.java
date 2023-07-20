package com.example.kp3coutsourcingproject.comment.repository;

import com.example.kp3coutsourcingproject.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentCustomRepository{

	@Override
	public List<Comment> getCommentsByPostId(Long postId) {
		return null;
	}
}
