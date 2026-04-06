package com.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.dto.UserRequestDto;
import com.project.dto.UserResponseDto;
@Service
public interface UserService {

    UserResponseDto createUser(UserRequestDto dto);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    void deleteUser(Long id);

	UserResponseDto createUser1(UserRequestDto dto);
}