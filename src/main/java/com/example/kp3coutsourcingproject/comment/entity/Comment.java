package com.example.kp3coutsourcingproject.comment.entity;

import com.example.kp3coutsourcingproject.comment.dto.CommentRequestDto;
import com.example.kp3coutsourcingproject.common.dto.Timestamped;
import com.example.kp3coutsourcingproject.post.entity.Post;
import com.example.kp3coutsourcingproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postId", nullable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentCommentId")
	private Comment parentComment;

	@Column(nullable = false)
	private String content;

	@Builder.Default
	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	private List<Comment> children = new ArrayList<>();

	public Comment(User user, Post post, Comment parent, String content) {
		this.post = post;
		this.user = user;
		this.parentComment = parent;
		this.content = content;
	}
}
