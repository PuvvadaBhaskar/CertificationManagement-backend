package com.project.service;

import com.project.model.RefreshToken;
import com.project.model.User;
import com.project.repo.RefreshTokenRepository;
import com.project.repo.UserRepository;
import com.project.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Map<String, Object> login(Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail());

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .map(existingToken -> {
                    existingToken.setUser(user); 
                    existingToken.setToken(UUID.randomUUID().toString());
                    existingToken.setExpiryDate(LocalDateTime.now().plusDays(7));
                    return existingToken;
                })
                .orElseGet(() -> {
                    RefreshToken newToken = new RefreshToken();
                    newToken.setUser(user);
                    newToken.setToken(UUID.randomUUID().toString());
                    newToken.setExpiryDate(LocalDateTime.now().plusDays(7));
                    return newToken;
                });

        refreshTokenRepository.save(refreshToken);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken.getToken(),
                "userId", user.getId(),
                "role", user.getRole(),
                "name", user.getName() != null ? user.getName() : "User", 
                "email", user.getEmail()
        );
    }

    @Override
    public String refresh(String refreshToken) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Expired refresh token");
        }

        return jwtUtil.generateToken(token.getUser().getEmail());
    }

    @Override
    public void logout(String refreshToken) {

        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public String register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return "User registered successfully";
    }
}