package com.example.kp3coutsourcingproject.oauth2.google.controller;

import com.example.kp3coutsourcingproject.oauth2.google.service.GoogleLoginService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class GoogleLoginController {

    GoogleLoginService loginService;

    public GoogleLoginController(GoogleLoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/code/{registrationId}")
    public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        loginService.socialLogin(code, registrationId);
    }
}
