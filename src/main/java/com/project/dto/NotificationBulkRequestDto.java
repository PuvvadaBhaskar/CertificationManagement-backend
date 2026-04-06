package com.project.dto;

import lombok.Data;
import java.util.List;

@Data
public class NotificationBulkRequestDto {

    private List<Long> userIds;
    private String title;
    private String message;
    private String channel;
    private Long senderId;
}