package com.project.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.project.dto.CertificationRequestDto;
import com.project.dto.CertificationResponseDto;
import com.project.dto.NotificationBulkRequestDto;
import com.project.dto.NotificationRequestDto;
import com.project.dto.NotificationResponseDto;
import com.project.exception.ResourceNotFoundException;
import com.project.model.DeliveryChannel;
import com.project.model.Notification;
import com.project.model.User;
import com.project.repo.NotificationRepository;
import com.project.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    //WRAPPER → uses core method
    @Override
    public void createNotification(Long userId, String message) {

        NotificationRequestDto dto = new NotificationRequestDto();
        dto.setUserId(userId);
        dto.setMessage(message);
        dto.setChannel("IN_APP");

        sendNotification(dto); //  reuseed core logic
    }

    @Override
    public void sendEmailToUser(Long userId, String subject, String message) {

        NotificationRequestDto dto = new NotificationRequestDto();
        dto.setUserId(userId);
        dto.setTitle(subject);
        dto.setMessage(message);
        dto.setChannel("EMAIL");

        sendNotification(dto); // reused core logic
    }

 
    @Override
    public List<NotificationResponseDto> getUserNotifications(Long userId) {

        return notificationRepository
                .findByUserId(userId, PageRequest.of(0, 10, Sort.by("createdAt").descending()))
                .stream()
                .map(n -> NotificationResponseDto.builder()
                        .id(n.getId())
                        .title(n.getTitle())
                        .message(n.getMessage())
                        .isRead(n.isRead())
                        .createdAt(n.getCreatedAt())
                        .senderId(n.getSenderId())
                        .channel(n.getChannel() != null ? n.getChannel().name() : "IN_APP")
                        .build())
                .toList();
    }

    @Override
    public void markAsRead(Long id) {

        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        n.setRead(true);
        notificationRepository.save(n);
    }

    @Override
    public void deleteNotification(Long id) {

        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found");
        }

        notificationRepository.deleteById(id);
    }

    public CertificationResponseDto addCertification(CertificationRequestDto dto) {
        return null;
    }
    // core method

    @Override
    public Map<String, Object> sendNotification(NotificationRequestDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        DeliveryChannel channel;
        try {
            channel = dto.getChannel() == null
                    ? DeliveryChannel.IN_APP
                    : DeliveryChannel.valueOf(dto.getChannel().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid channel type");
        }

        String finalMessage = (dto.getTitle() != null && !dto.getTitle().isEmpty())
                ? dto.getTitle() + ": " + dto.getMessage()
                : dto.getMessage();

        Notification notification = Notification.builder()
                .user(user)
                .title(dto.getTitle())
                .message(finalMessage)
                .senderId(dto.getSenderId())
                .channel(channel)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        boolean emailSent = false;

        if (channel == DeliveryChannel.EMAIL) {
            try {
                String body = "Hello " + user.getName() + ",\n\n" +
                        finalMessage + "\n\nRegards,\nAdmin";

                emailService.sendEmail(user.getEmail(), dto.getTitle(), body);
                emailSent = true;

            } catch (Exception e) {
                e.printStackTrace(); 
                throw new RuntimeException("Email failed: " + e.getMessage());
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("notificationId", notification.getId());
        response.put("emailSent", emailSent);

        return response;
    }
    @Override
    public Map<String, Object> sendBulkNotification(NotificationBulkRequestDto dto) {

        // ✅ SAFETY CHECK (VERY IMPORTANT)
        if (dto.getUserIds() == null || dto.getUserIds().isEmpty()) {
            throw new RuntimeException("userIds cannot be null or empty");
        }

        int successCount = 0;

        for (Long userId : dto.getUserIds()) {

            if (userId == null) continue; // extra safety condition used

            NotificationRequestDto singleDto = new NotificationRequestDto();
            singleDto.setUserId(userId);
            singleDto.setTitle(dto.getTitle());
            singleDto.setMessage(dto.getMessage());
            singleDto.setChannel(dto.getChannel());
            singleDto.setSenderId(dto.getSenderId());

            sendNotification(singleDto); 
            successCount++;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("totalSent", successCount);

        return response;
    }
}