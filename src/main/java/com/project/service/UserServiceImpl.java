package com.project.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.dto.UserRequestDto;
import com.project.dto.UserResponseDto;
import com.project.exception.ResourceNotFoundException;
import com.project.model.User;
import com.project.repo.CertificationRepository;
import com.project.repo.NotificationRepository;
import com.project.repo.RefreshTokenRepository;
import com.project.repo.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationRepository notificationRepository;
    private final CertificationRepository certificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);

        return new UserResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRole()
        );
    }

    @Override
    public UserResponseDto getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(u -> new UserResponseDto(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole()))
                .toList();
    }

    
    @Override
    @Transactional   // ✅ ADD THIS

    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }

        try {
            notificationRepository.deleteByUserId(id);

            certificationRepository.deleteById(id);

            userRepository.deleteById(id);
            refreshTokenRepository.deleteById(id);

            System.out.println("✅ User deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }
    @Override
    public UserResponseDto createUser1(UserRequestDto dto) {
        return createUser(dto);
    }
}