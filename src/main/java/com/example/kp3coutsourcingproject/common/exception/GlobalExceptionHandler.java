package com.example.kp3coutsourcingproject.common.exception;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.example")
public class GlobalExceptionHandler {

	// 에러 처리
	@ExceptionHandler({CustomException.class})
	private ResponseEntity<ApiResponseDto> handleEnityNotFoundException(EntityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
	}

}
