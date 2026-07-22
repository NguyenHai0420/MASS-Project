package com.group_project.MASS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecialtyStatsResponse {
    private Long specialtyId;
    private String specialtyName;
    private long totalDoctors;
    private long totalAppointments;
    private long completedAppointments;
}
