package com.group_project.MASS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {
    private Long id;
    private Long appointmentId;
    private String patientName;
    private String diagnosis;
    private String notes;
    private String prescription;
    private LocalDateTime createdAt;
}
