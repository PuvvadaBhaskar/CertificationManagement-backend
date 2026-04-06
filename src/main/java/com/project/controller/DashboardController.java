package com.project.controller;

import org.springframework.web.bind.annotation.*;

import com.project.dto.DashboardResponseDto;
import com.project.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/user/{userId}")
    public DashboardResponseDto getUserDashboard(
            @PathVariable Long userId) {

        return dashboardService.getUserDashboard(userId);
    }
}