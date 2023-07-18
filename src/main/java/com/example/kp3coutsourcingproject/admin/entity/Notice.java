package com.example.kp3coutsourcingproject.admin.entity;

import com.example.kp3coutsourcingproject.admin.dto.AdminPostRequestDto;
import com.example.kp3coutsourcingproject.admin.dto.AdminUserRequestDto;
import com.example.kp3coutsourcingproject.common.dto.Timestamped;
import com.example.kp3coutsourcingproject.user.entity.User;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "notice")
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


    public Notice(AdminPostRequestDto requestDto, User user) {
        this.content = requestDto.getContent();
        this.user = user;
    }
}
