package com.project.dto;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class NotificationRequestDto {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Message is required")
    @Size(min = 5, max = 500, message = "Message must be between 5 and 500 characters")
    private String message;

    @NotNull(message = "Sender ID is required")
    @Positive(message = "Sender ID must be positive")
    private Long senderId;

    private boolean sendEmail;

    @NotBlank(message = "Channel is required")
    @Pattern(
        regexp = "EMAIL|SMS|PUSH",
        message = "Channel must be EMAIL, SMS, or PUSH"
    )
    private String channel;
}