package com.example.kp3coutsourcingproject.post.entity;

import com.example.kp3coutsourcingproject.common.dto.Timestamped;
import com.example.kp3coutsourcingproject.common.file.Post_Image;
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


	@OneToMany(mappedBy = "post",  cascade = CascadeType.ALL, orphanRemoval = true)// 게시글이 삭제되면 그 게시글에 있는 댓글들 모두 삭제가 되도록
	private List<Post_Image> ImagetList;

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
