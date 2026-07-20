package com.group_project.MASS.service;

import com.group_project.MASS.dto.AppointmentResponse;
import com.group_project.MASS.model.Appointment;
import com.group_project.MASS.model.DoctorProfile;
import com.group_project.MASS.repository.AppointmentRepository;
import com.group_project.MASS.repository.DoctorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    // Chuyển Appointment entity → AppointmentResponse DTO
    private AppointmentResponse toResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .patientName(a.getPatient().getFullName())
                .patientEmail(a.getPatient().getEmail())
                .reason(a.getReason())
                .status(a.getStatus().name())
                .scheduleDate(a.getSchedule().getDate())
                .scheduleStartTime(a.getSchedule().getStartTime())
                .scheduleEndTime(a.getSchedule().getEndTime())
                .createdAt(a.getCreatedAt())
                .build();
    }

    // Lấy tất cả appointment của doctor đang đăng nhập
    public List<AppointmentResponse> getMyAppointments(String email) {
        DoctorProfile dp = doctorProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DoctorProfile cho email: " + email));
        return appointmentRepository.findByDoctorProfileOrderByCreatedAtDesc(dp)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
