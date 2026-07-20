package com.group_project.MASS.service;

import com.group_project.MASS.dto.DashboardStatsResponse;
import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.model.Role;
import com.group_project.MASS.repository.AppointmentRepository;
import com.group_project.MASS.repository.DoctorProfileRepository;
import com.group_project.MASS.repository.SpecialtyRepository;
import com.group_project.MASS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Thống kê tổng quan cho Admin Dashboard
    public DashboardStatsResponse getDashboardStats() {
        long totalDoctors = doctorProfileRepository.count();
        long totalPatients = userRepository.countByRole(Role.ROLE_PATIENT);
        long totalAppointments = appointmentRepository.count();
        long totalSpecialties = specialtyRepository.count();
        long pendingAppointments = appointmentRepository.countByStatus(AppointmentStatus.PENDING);
        long completedAppointments = appointmentRepository.countByStatus(AppointmentStatus.COMPLETED);

        return DashboardStatsResponse.builder()
                .totalDoctors(totalDoctors)
                .totalPatients(totalPatients)
                .totalAppointments(totalAppointments)
                .totalSpecialties(totalSpecialties)
                .pendingAppointments(pendingAppointments)
                .completedAppointments(completedAppointments)
                .build();
    }
}
