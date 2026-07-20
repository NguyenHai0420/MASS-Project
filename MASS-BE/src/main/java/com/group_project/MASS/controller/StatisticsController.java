package com.group_project.MASS.controller;

import com.group_project.MASS.dto.DashboardStatsResponse;
import com.group_project.MASS.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    // GET /api/statistics/dashboard — Thống kê tổng quan cho Admin Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(statisticsService.getDashboardStats());
    }
}
