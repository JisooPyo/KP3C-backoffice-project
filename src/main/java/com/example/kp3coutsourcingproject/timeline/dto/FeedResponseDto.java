package com.example.kp3coutsourcingproject.timeline.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponseDto {
    private Long feedId;
    private List<FeedPostDto> postList = new ArrayList<>();
}
