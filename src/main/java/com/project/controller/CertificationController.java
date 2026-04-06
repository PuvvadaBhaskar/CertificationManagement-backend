package com.project.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.ApiResponse;
import com.project.dto.CertificationRequestDto;
import com.project.dto.CertificationResponseDto;
import com.project.service.CertificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping(consumes = "multipart/form-data")
    public CertificationResponseDto add(
            @RequestParam("title") String title,
            @RequestParam("organization") String organization,
            @RequestParam("issueDate") String issueDate,
            @RequestParam(value = "expiryDate", required = false) String expiryDate,
            @RequestParam("status") String status,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {

        // ✅ DEBUG
        System.out.println("FILE IN CONTROLLER: " + file);

        CertificationRequestDto dto = new CertificationRequestDto();
        dto.setTitle(title);
        dto.setOrganization(organization);

        dto.setIssueDate(LocalDate.parse(issueDate.substring(0, 10)));

        if (expiryDate != null && !expiryDate.isEmpty()) {
            dto.setExpiryDate(LocalDate.parse(expiryDate.substring(0, 10)));
        }

        dto.setStatus(status);

        return certificationService.addCertificationWithFile(dto, userId, file);
    }
    @GetMapping("/user/{userId}")
    public List<CertificationResponseDto> getByUser(@PathVariable Long userId) {
        return certificationService.getByUser(userId);
    }

    @PutMapping("/{id}")
    public CertificationResponseDto update(
            @PathVariable Long id,
            @RequestBody CertificationRequestDto dto) {

        return certificationService.updateCertification(id, dto);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        certificationService.deleteCertification(id);
        return "Deleted successfully";
    }

    @PutMapping("/{id}/renew")
    public CertificationResponseDto renew(
            @PathVariable Long id,
            @RequestParam String newDate) {

        return certificationService.renewCertification(id, LocalDate.parse(newDate));
    }

    @GetMapping
    public ApiResponse<Page<CertificationResponseDto>> getAllCertifications(

            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {

        Page<CertificationResponseDto> result =
                certificationService.getAllCertifications(
                        userId, page, size, sortBy, search, status
                );

        return new ApiResponse<>(true, result, "Fetched successfully");
    }
}