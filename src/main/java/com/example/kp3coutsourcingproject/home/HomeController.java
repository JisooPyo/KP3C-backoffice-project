package com.example.kp3coutsourcingproject.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/kp3c/user")
    public String home() {
        return "index";
    }
}