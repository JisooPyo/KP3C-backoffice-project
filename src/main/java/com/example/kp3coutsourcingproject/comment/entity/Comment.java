package com.example.kp3coutsourcingproject.comment.entity;

import com.example.kp3coutsourcingproject.post.entity.Post;
import com.example.kp3coutsourcingproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
public class Comment {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postId",nullable = false)
	private Post post;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false,length = 280)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId",nullable = false)
	private User user;


}
/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parentId")
	private Post parent;

	@Builder.Default
	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	private List<Post> children = new ArrayList<>();
 */