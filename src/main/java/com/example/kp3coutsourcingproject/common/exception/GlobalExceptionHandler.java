package com.example.kp3coutsourcingproject.common.exception;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.example")
public class GlobalExceptionHandler {
	@ExceptionHandler(CustomException.class)
	private ResponseEntity<ApiResponseDto> handleCustomException(CustomException ex) {
		return ResponseEntity
				.status(ex.getErrorCode().getStatus())
				.body(new ApiResponseDto(ex.getMessage(), ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler({Exception.class})
	private ResponseEntity<ApiResponseDto> handleException(Exception ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST.value())
				.body(new ApiResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}
}
