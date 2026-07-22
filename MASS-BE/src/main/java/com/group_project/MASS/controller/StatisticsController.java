package com.group_project.MASS.controller;

import com.group_project.MASS.dto.response.DashboardStatsResponse;
import com.group_project.MASS.dto.response.DoctorStatsResponse;
import com.group_project.MASS.dto.response.PatientStatsResponse;
import com.group_project.MASS.dto.response.SpecialtyStatsResponse;
import com.group_project.MASS.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        return ResponseEntity.ok(statisticsService.getDashboardStats());
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorStatsResponse>> getDoctorStats() {
        return ResponseEntity.ok(statisticsService.getDoctorStats());
    }

    @GetMapping("/patients")
    public ResponseEntity<PatientStatsResponse> getPatientStats() {
        return ResponseEntity.ok(statisticsService.getPatientStats());
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<SpecialtyStatsResponse>> getSpecialtyStats() {
        return ResponseEntity.ok(statisticsService.getSpecialtyStats());
    }
}
