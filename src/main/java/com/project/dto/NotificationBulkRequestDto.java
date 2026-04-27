package com.project.dto;

import lombok.Data;
import java.util.List;

import jakarta.validation.constraints.*;

@Data
public class NotificationBulkRequestDto {

    @NotEmpty(message = "User IDs list cannot be empty")
    private List<
            @NotNull(message = "User ID cannot be null")
            @Positive(message = "User ID must be positive")
            Long> userIds;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Message is required")
    @Size(min = 5, max = 500, message = "Message must be between 5 and 500 characters")
    private String message;

    @NotBlank(message = "Channel is required")
    @Pattern(
        regexp = "EMAIL|SMS|PUSH",
        message = "Channel must be EMAIL, SMS, or PUSH"
    )
    private String channel;

    @NotNull(message = "Sender ID is required")
    @Positive(message = "Sender ID must be positive")
    private Long senderId;
}