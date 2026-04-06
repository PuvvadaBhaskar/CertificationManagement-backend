package com.project.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String organization;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private String status;
    @Column(name = "file_path")
    private String filePath;    

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}