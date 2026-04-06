package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CertificationResponseDto {

    private Long id;
    private String title;
    private String organization;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String status;
    private Long userId;
    private String userName;
    private String filePath;
}