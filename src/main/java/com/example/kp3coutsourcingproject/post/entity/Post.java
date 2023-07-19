package com.example.kp3coutsourcingproject.post.entity;

import com.example.kp3coutsourcingproject.common.dto.Timestamped;
import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "posts")
@NoArgsConstructor
public class Post extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@Column(nullable = false)
	private String content;

	public Post(PostRequestDto requestDto) {
		this.content = requestDto.getContent();
	}

	public void update(PostRequestDto requestDto) {
		this.content = requestDto.getContent();
	}
}
