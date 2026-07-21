package com.group_project.MASS.dto.response;

import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.model.AppointmentType;
import com.group_project.MASS.model.PaymentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentListResponse {
    private Long appointmentId;
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private Long doctorProfileId;
    private String doctorName;
    private Long specialtyId;
    private String specialtyName;
    private Long scheduleId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer queueNumber;
    private AppointmentType appointmentType;
    private AppointmentStatus appointmentStatus;
    private PaymentStatus paymentStatus;
    private boolean paid;
}
