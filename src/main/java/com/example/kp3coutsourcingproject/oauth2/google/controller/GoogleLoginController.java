package com.example.kp3coutsourcingproject.oauth2.google.controller;

import com.example.kp3coutsourcingproject.oauth2.google.service.GoogleLoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.PresentationDirection;

@Controller
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class GoogleLoginController {

    GoogleLoginService loginService;

    public GoogleLoginController(GoogleLoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping(value = "/code/{registrationId}", produces = "application/json")
    public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        loginService.socialLogin(code, registrationId);
    }

}
