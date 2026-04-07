package com.project.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.project.dto.DashboardResponseDto;
import com.project.repo.CertificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CertificationRepository certificationRepository;

    @Override
    public DashboardResponseDto getUserDashboard(Long userId) {

        LocalDate today = LocalDate.now();
        LocalDate next7Days = today.plusDays(7);

        long total = certificationRepository.countByUser_Id(userId);

        long expired = certificationRepository
                .countByUser_IdAndExpiryDateBefore(userId, today);

        long expiringSoon = certificationRepository
                .countByUser_IdAndExpiryDateBetween(userId, today, next7Days);

        long active = total - expired;

        return new DashboardResponseDto(
                total,
                active,
                expired,
                expiringSoon
        );
    }
}