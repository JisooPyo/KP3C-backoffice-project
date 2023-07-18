package com.example.kp3coutsourcingproject.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 응답 값으로 파싱이 될 때, 변환이 될 때 JSON 형태로 변환이 되는데 그 때 non_null인 값들만 반환!!
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {
	private String msg;
	private Integer statusCode;

	public ApiResponseDto(String msg, Integer statusCode) {
		this.msg = msg;
		this.statusCode = statusCode;
	}
}
