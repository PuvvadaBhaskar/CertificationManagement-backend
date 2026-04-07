package com.project.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.CertificationRequestDto;
import com.project.dto.CertificationResponseDto;
import com.project.exception.ResourceNotFoundException;
import com.project.model.Certification;
import com.project.model.User;
import com.project.repo.CertificationRepository;
import com.project.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {

    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public CertificationResponseDto addCertification(CertificationRequestDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Certification cert = new Certification();
        cert.setTitle(dto.getTitle());
        cert.setOrganization(dto.getOrganization());
        cert.setIssueDate(dto.getIssueDate());
        cert.setExpiryDate(dto.getExpiryDate());
        cert.setStatus(dto.getStatus());
        cert.setUser(user);

        Certification saved = certificationRepository.save(cert);

        return mapToDto(saved);
    }

    @Override
    public CertificationResponseDto addCertificationWithFile(
            CertificationRequestDto dto,
            Long userId,
            MultipartFile file) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String filePath = null;

        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                filePath = uploadDir + fileName;
                file.transferTo(new File(filePath));
            }

        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }

        Certification cert = new Certification();
        cert.setTitle(dto.getTitle());
        cert.setOrganization(dto.getOrganization());
        cert.setIssueDate(dto.getIssueDate());
        cert.setExpiryDate(dto.getExpiryDate());
        cert.setStatus(dto.getStatus());
        cert.setUser(user);
        cert.setFilePath(filePath);

        Certification saved = certificationRepository.save(cert);

        return mapToDto(saved);
    }

    @Override
    public List<CertificationResponseDto> getByUser(Long userId) {
        return certificationRepository.findByUser_Id(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public CertificationResponseDto updateCertification(Long id, CertificationRequestDto dto) {

        Certification cert = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found"));

        cert.setTitle(dto.getTitle());
        cert.setOrganization(dto.getOrganization());
        cert.setIssueDate(dto.getIssueDate());
        cert.setExpiryDate(dto.getExpiryDate());
        cert.setStatus(dto.getStatus());

        return mapToDto(certificationRepository.save(cert));
    }

    @Override
    public void deleteCertification(Long id) {

        if (!certificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Certification not found");
        }

        certificationRepository.deleteById(id);
    }

    @Override
    public CertificationResponseDto renewCertification(Long id, LocalDate newExpiry) {

        Certification cert = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found"));

        cert.setExpiryDate(newExpiry);
        cert.setStatus("ACTIVE");

        return mapToDto(certificationRepository.save(cert));
    }

    @Override
    public Page<CertificationResponseDto> getAllCertifications(
            Long userId,
            int page,
            int size,
            String sortBy,
            String search,
            String status
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        Page<Certification> certPage;

        if (userId != null) {
            certPage = certificationRepository.findByUser_Id(userId, pageable);
        } else {
            certPage = certificationRepository.findAll(pageable);
        }

        return certPage.map(this::mapToDto);
    }

    private CertificationResponseDto mapToDto(Certification cert) {
        return new CertificationResponseDto(
                cert.getId(),
                cert.getTitle(),
                cert.getOrganization(),
                cert.getIssueDate(),
                cert.getExpiryDate(),
                cert.getStatus(),
                cert.getUser().getId(),
                cert.getUser().getName(),
                cert.getFilePath()
        );
    }
    public Page<Certification> getCertificationsByUser(Long userId, Pageable pageable) {
        return certificationRepository.findByUser_Id(userId, pageable);
    }
}