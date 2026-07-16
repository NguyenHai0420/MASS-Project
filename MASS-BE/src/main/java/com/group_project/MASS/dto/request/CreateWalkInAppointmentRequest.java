package com.group_project.MASS.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalkInAppointmentRequest {
    @NotNull(message = "ID của bệnh nhân không được để trống")
    private Long patientId;

    @NotNull(message = "chuyên khoa không được để trống")
    private Long specialtyId;

    private Long doctorProfileId;

    @NotNull(message = "Ngày khám không được để trống")
    @FutureOrPresent(message = "Ngày khám không được nhỏ hơn ngày hiện tại")
    private LocalDate appointmentDate;

    private LocalTime createdTime;

    @NotBlank(message = "Lý do khám không được để trống")
    private String reason;
}
