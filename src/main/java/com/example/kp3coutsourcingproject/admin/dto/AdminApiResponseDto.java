package com.example.kp3coutsourcingproject.admin.dto;

public class AdminApiResponseDto {

    private Integer code;
    private String msg;

    public AdminApiResponseDto(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
