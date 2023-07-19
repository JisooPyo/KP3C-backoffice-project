package com.example.kp3coutsourcingproject.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@Data
public class UserProfileRequestDto {
    private String nickname;
    private String introduction;
    private MultipartFile imageFile;
}
