package com.project.dto;

import lombok.Data;

@Data
public class NotificationRequestDto {

    private Long userId;
    private String title;
    private String message;
    private Long senderId;
    private boolean sendEmail;
    private String channel;
}