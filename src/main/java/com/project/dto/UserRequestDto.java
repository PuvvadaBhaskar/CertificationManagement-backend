package com.project.dto;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class UserRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Role is required")
    @Pattern(
        regexp = "ADMIN|USER|MANAGER",
        message = "Role must be ADMIN, USER, or MANAGER"
    )
    private String role;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
        message = "Password must contain uppercase, lowercase, number, and special character"
    )
    private String password;
}