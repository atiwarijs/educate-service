package com.eduservices.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @GetMapping("/status")
    public String getStatus() {
        return "Security Service is running!";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
