package com.group_project.MASS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private String patientName;
    private String patientEmail;
    private String reason;
    private String status;         // PENDING, CONFIRMED, COMPLETED, CANCELLED

    // Thông tin lịch hẹn
    private LocalDate scheduleDate;
    private LocalTime scheduleStartTime;
    private LocalTime scheduleEndTime;

    private LocalDateTime createdAt;
}
