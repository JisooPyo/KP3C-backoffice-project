package com.example.kp3coutsourcingproject.post.entity;

import com.example.kp3coutsourcingproject.common.dto.Timestamped;
import com.example.kp3coutsourcingproject.common.file.Post_Image;
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

	@OneToMany(mappedBy = "post",  cascade = CascadeType.ALL, orphanRemoval = true)// 게시글이 삭제되면 그 게시글에 있는 댓글들 모두 삭제가 되도록
	private List<Post_Image> ImagetList;

	public Post(PostRequestDto requestDto) {
		this.content = requestDto.getContent();
	}
}
