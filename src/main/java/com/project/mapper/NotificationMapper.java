package com.project.mapper;

import com.project.dto.NotificationResponseDto;
import com.project.model.Notification;

public class NotificationMapper {

    public static NotificationResponseDto toDto(Notification n) {
        return NotificationResponseDto.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .senderId(n.getSenderId())
                .channel(n.getChannel().name())
                .build();
    }
}