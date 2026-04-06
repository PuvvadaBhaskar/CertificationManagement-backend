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

        long total = certificationRepository.countByUserId(userId);

        long expired = certificationRepository
                .countByUserIdAndExpiryDateBefore(userId, today);

        long expiringSoon = certificationRepository
                .countByUserIdAndExpiryDateBetween(userId, today, next7Days);

        long active = total - expired;

        return new DashboardResponseDto(
                total,
                active,
                expired,
                expiringSoon
        );
    }
}
