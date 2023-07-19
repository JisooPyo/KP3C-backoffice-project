package com.example.kp3coutsourcingproject.admin.dto;

import com.example.kp3coutsourcingproject.admin.entity.Notice;
import com.example.kp3coutsourcingproject.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminNoticeResponseDto {

    private Long id;
    private UserRoleEnum role;
    private String username;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;


    public AdminNoticeResponseDto(Notice notice) {
        this.id = notice.getId();
        this.role = notice.getUser().getRole();
        this.username = notice.getUser().getUsername();
        this.nickname = notice.getUser().getNickname();
        this.content = notice.getContent();
        this.createdAt = notice.getCreatedAt();
    }
}
