package com.project.service;

import java.util.List;
import java.util.Map;

import com.project.dto.CertificationRequestDto;
import com.project.dto.CertificationResponseDto;
import com.project.dto.NotificationBulkRequestDto;
import com.project.dto.NotificationRequestDto;
import com.project.dto.NotificationResponseDto;

public interface NotificationService {

    Map<String, Object> sendNotification(NotificationRequestDto dto);

    // Wrapper methods (optional usage)
    void createNotification(Long userId, String message);

    void sendEmailToUser(Long userId, String subject, String message);

    List<NotificationResponseDto> getUserNotifications(Long userId);

    void markAsRead(Long id);

    Map<String, Object> sendBulkNotification(NotificationBulkRequestDto dto);
    void deleteNotification(Long id);
}