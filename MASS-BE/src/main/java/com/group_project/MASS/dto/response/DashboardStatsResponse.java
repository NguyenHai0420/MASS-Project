package com.group_project.MASS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalDoctors;
    private long totalPatients;
    private long totalAppointments;
    private long totalSpecialties;
    private long pendingAppointments;
    private long completedAppointments;
}
