package com.example.kp3coutsourcingproject.post.entity;

import com.example.kp3coutsourcingproject.common.dto.Timestamped;
import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Setter
@Getter
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentId")
	private Post parent;

	@Column(nullable = false)
	private String content;

	@Builder.Default
	@OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
	private List<Post> children = new ArrayList<>();

	public void update(PostRequestDto requestDto) {
		this.content = requestDto.getContent();
	}

	public void updateParent(Post parent) {
		this.parent = parent;
	}

	public Post(PostRequestDto requestDto) {
		this.content=requestDto.getContent();
	}
}
