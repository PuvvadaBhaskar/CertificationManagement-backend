package com.project.dto;

import lombok.Data;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

@Data
public class CertificationRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Organization is required")
    @Size(min = 2, max = 100, message = "Organization name must be between 2 and 100 characters")
    private String organization;

    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "Issue date cannot be in the future")
    private LocalDate issueDate;

    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    @NotBlank(message = "Status is required")
    @Pattern(
        regexp = "ACTIVE|EXPIRED|PENDING",
        message = "Status must be ACTIVE, EXPIRED, or PENDING"
    )
    private String status;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;
}