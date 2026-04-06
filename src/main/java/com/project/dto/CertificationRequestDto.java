package com.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CertificationRequestDto {

    private String title;
    private String organization;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String status;
    private Long userId;
}