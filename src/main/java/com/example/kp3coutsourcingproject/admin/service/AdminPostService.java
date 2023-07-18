package com.example.kp3coutsourcingproject.admin.service;

import com.example.kp3coutsourcingproject.admin.entity.Notice;
import com.example.kp3coutsourcingproject.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    public List<PostResponseDto> getPosts() {
        return null;
    }

    public PostResponseDto getPost() {
        return null;
    }

    public PostResponseDto deletePost() {
        return null;
    }

    public void createNotice() {
        Notice notice = new Notice();
    }
}
