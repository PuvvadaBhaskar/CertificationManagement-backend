package com.project.controller;

import com.project.model.User;
import com.project.service.AuthService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public String refresh(@RequestParam String refreshToken) {
        return authService.refresh(refreshToken);
    }

    @DeleteMapping("/logout")
    public void logout(@RequestParam String refreshToken) {
        authService.logout(refreshToken);
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.register(user);
    }
    
    @GetMapping("/home")
    public String home()
    {
    	return "Home";
    }
}