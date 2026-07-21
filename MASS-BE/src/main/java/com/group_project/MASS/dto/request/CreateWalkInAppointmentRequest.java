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
    @NotBlank(message = "Tên bệnh nhân không được để trống")
    private String patientName;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String patientPhone;

    @NotBlank(message = "Email bệnh nhân không được để trống")
    private String patientEmail;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotNull(message = "chuyên khoa không được để trống")
    private Long specialtyId;

    private Long doctorProfileId;

    @NotNull(message = "Ngày khám không được để trống")
    @FutureOrPresent(message = "Ngày khám không được nhỏ hơn ngày hiện tại")
    private LocalDate appointmentDate;

    private LocalTime searchFromTime;

    @NotBlank(message = "Lý do khám không được để trống")
    private String reason;
}
