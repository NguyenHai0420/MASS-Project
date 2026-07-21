package com.group_project.MASS.service;

import com.group_project.MASS.dto.AppointmentDto;
import com.group_project.MASS.dto.AppointmentRequestDto;
import com.group_project.MASS.dto.RescheduleRequestDto;
import java.util.List;

public interface AppointmentService {
    AppointmentDto bookAppointment(AppointmentRequestDto request, String patientEmail);
    List<AppointmentDto> getMyAppointments(String patientEmail);
    AppointmentDto cancelAppointment(Long appointmentId, String patientEmail);
    AppointmentDto rescheduleAppointment(Long appointmentId, RescheduleRequestDto request, String patientEmail);
}
