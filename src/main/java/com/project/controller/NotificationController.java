package com.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.dto.NotificationBulkRequestDto;
import com.project.dto.NotificationRequestDto;
import com.project.dto.NotificationResponseDto;
import com.project.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // ✅ React
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDto>> getUserNotifications(@PathVariable Long userId) {

        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {

        notificationService.markAsRead(id);
        return ResponseEntity.ok("Marked as read");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody NotificationRequestDto dto) {

        return ResponseEntity.ok(notificationService.sendNotification(dto));
    }
    @PostMapping("/bulk")
    public ResponseEntity<Map<String, Object>> sendBulkNotification(
            @RequestBody NotificationBulkRequestDto dto) {

        return ResponseEntity.ok(notificationService.sendBulkNotification(dto));
    }
}