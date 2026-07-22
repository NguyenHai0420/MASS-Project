package com.group_project.MASS.service;

import com.group_project.MASS.dto.response.DashboardStatsResponse;
import com.group_project.MASS.dto.response.DoctorStatsResponse;
import com.group_project.MASS.dto.response.PatientStatsResponse;
import com.group_project.MASS.dto.response.SpecialtyStatsResponse;

import java.util.List;

public interface StatisticsService {
    DashboardStatsResponse getDashboardStats();
    List<DoctorStatsResponse> getDoctorStats();
    PatientStatsResponse getPatientStats();
    List<SpecialtyStatsResponse> getSpecialtyStats();
}
