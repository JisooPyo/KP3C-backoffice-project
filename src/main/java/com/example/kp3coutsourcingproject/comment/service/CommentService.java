package com.example.kp3coutsourcingproject.comment.service;

import com.example.kp3coutsourcingproject.comment.repository.CommentRepository;
import com.example.kp3coutsourcingproject.post.repository.PostRepository;
import com.example.kp3coutsourcingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
}
