package com.group_project.MASS.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalRecordRequest {
    @NotNull(message = "appointmentId không được để trống")
    private Long appointmentId;

    @NotBlank(message = "Chẩn đoán không được để trống")
    private String diagnosis;

    private String notes;
    private String prescription;
}
