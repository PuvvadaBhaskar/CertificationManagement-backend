package com.project.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.CertificationRequestDto;
import com.project.dto.CertificationResponseDto;
import com.project.model.Certification;

public interface CertificationService {

    CertificationResponseDto addCertification(CertificationRequestDto dto);

    CertificationResponseDto addCertificationWithFile(
            CertificationRequestDto dto,
            Long userId,
            MultipartFile file
    );

    List<CertificationResponseDto> getByUser(Long userId);

    CertificationResponseDto updateCertification(Long id, CertificationRequestDto dto);

    void deleteCertification(Long id);

    CertificationResponseDto renewCertification(Long id, LocalDate newExpiry);

    Page<CertificationResponseDto> getAllCertifications(
            Long userId,
            int page,
            int size,
            String sortBy,
            String search,
            String status
    );
    Page<Certification> getCertificationsByUser(Long userId, Pageable pageable);

}