package com.example.kp3coutsourcingproject.admin.entity;

import com.example.kp3coutsourcingproject.common.dto.Timestamped;
import com.example.kp3coutsourcingproject.post.dto.PostRequestDto;
import com.example.kp3coutsourcingproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notices")
@NoArgsConstructor
public class Notice extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // 회원정보가 필요할 때만 가져올 수 있다
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Notice(PostRequestDto requestDto, User user) {
        this.content = requestDto.getContent();
        this.user = user;
    }
}
