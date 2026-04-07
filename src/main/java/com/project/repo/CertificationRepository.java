package com.project.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.Certification;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    List<Certification> findByUser_Id(Long userId);

    Page<Certification> findByUser_Id(Long userId, Pageable pageable);

    long countByUser_Id(Long userId);

    long countByUser_IdAndExpiryDateBefore(Long userId, LocalDate date);

    long countByUser_IdAndExpiryDateAfter(Long userId, LocalDate date);

    long countByUser_IdAndExpiryDateBetween(Long userId, LocalDate start, LocalDate end);

    Page<Certification> findByUser_IdAndTitleContainingIgnoreCase(
            Long userId, String title, Pageable pageable);

    Page<Certification> findByUser_IdAndStatus(
            Long userId, String status, Pageable pageable);

    void deleteByUser_Id(Long userId);
}