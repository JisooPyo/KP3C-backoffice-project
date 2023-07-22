package com.example.kp3coutsourcingproject.common.jwt;

import com.example.kp3coutsourcingproject.common.dto.ApiResponseDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        // throw exception 비교
        if (exception instanceof AuthenticationServiceException) {
            request.setAttribute("loginFailMsg", "존재하지 않는 사용자입니다.");
        } else if(exception instanceof BadCredentialsException) {
            request.setAttribute("loginFailMsg", "아이디 또는 비밀번호가 틀립니다.");
        } else if(exception instanceof LockedException) {
            request.setAttribute("loginFailMsg", "잠긴 계정입니다. 관리자에게 문의하세요.");
        } else if(exception instanceof DisabledException) {
            request.setAttribute("loginFailMsg", "비활성화된 계정입니다. 관리자에게 문의하세요.");
        } else if(exception instanceof AccountExpiredException) {
            request.setAttribute("loginFailMsg", "만료된 계정입니다. 관리자에게 문의하세요.");
        } else if(exception instanceof CredentialsExpiredException) {
            request.setAttribute("loginFailMsg", "비밀번호가 만료되었습니다.");
        }

        // client에 응답하기 "message" = "loginFailMsg".value / "status" = "400"
        ApiResponseDto apiResponseDto = new ApiResponseDto(request.getAttribute("loginFailMsg").toString(), response.getStatus());
        String jsonResponseBody = new ObjectMapper().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true).writeValueAsString(apiResponseDto);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponseBody);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
