package com.project.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
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

    private final String uploadDir = "uploads/";

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

        if (saved.getExpiryDate() != null &&
                saved.getExpiryDate().isBefore(LocalDate.now().plusDays(7))) {

            notificationService.createNotification(
                    user.getId(),
                    "Your certification '" + saved.getTitle() + "' is expiring soon!"
            );
        }

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

            // ✅ FIX: handle null file safely
            if (file != null && !file.isEmpty()) {

                String originalName = file.getOriginalFilename();

                if (originalName == null) {
                    throw new RuntimeException("File name is null");
                }

                String fileName = UUID.randomUUID() + "_" + originalName;
                filePath = uploadDir + fileName;

                file.transferTo(new File(filePath));

                System.out.println("File saved at: " + filePath);

            } else {
                System.out.println("⚠ No file uploaded");
            }

        } catch (IOException e) {
            e.printStackTrace(); // 🔥 VERY IMPORTANT
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

        if (saved.getExpiryDate() != null &&
                saved.getExpiryDate().isBefore(LocalDate.now().plusDays(7))) {

            notificationService.createNotification(
                    user.getId(),
                    "Your certification '" + saved.getTitle() + "' is expiring soon!"
            );
        }

        return mapToDto(saved);
    }

    @Override
    public List<CertificationResponseDto> getByUser(Long userId) {

        return certificationRepository.findByUserId(userId)
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

        Certification updated = certificationRepository.save(cert);

        if (updated.getExpiryDate() != null &&
                updated.getExpiryDate().isBefore(LocalDate.now().plusDays(7))) {

            notificationService.createNotification(
                    updated.getUser().getId(),
                    "Your certification '" + updated.getTitle() + "' is expiring soon!"
            );
        }

        return mapToDto(updated);
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

        Certification updated = certificationRepository.save(cert);

        notificationService.createNotification(
                cert.getUser().getId(),
                "Your certification '" + cert.getTitle() + "' has been renewed."
        );

        return mapToDto(updated);
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

    @Override
    public Page<CertificationResponseDto> getAllCertifications(Long userId, int page, int size, String sortBy,
            String search, String status) {
        return Page.empty();
    }
}