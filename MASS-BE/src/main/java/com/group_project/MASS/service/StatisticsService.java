package com.group_project.MASS.service;

import com.group_project.MASS.dto.DashboardStatsResponse;
import com.group_project.MASS.dto.DoctorStatsResponse;
import com.group_project.MASS.dto.PatientStatsResponse;
import com.group_project.MASS.dto.SpecialtyStatsResponse;

import java.util.List;

public interface StatisticsService {
    DashboardStatsResponse getDashboardStats();
    List<DoctorStatsResponse> getDoctorStats();
    PatientStatsResponse getPatientStats();
    List<SpecialtyStatsResponse> getSpecialtyStats();
}
