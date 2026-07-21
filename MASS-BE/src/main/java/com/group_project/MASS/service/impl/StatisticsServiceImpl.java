package com.group_project.MASS.service.impl;

import com.group_project.MASS.dto.DashboardStatsResponse;
import com.group_project.MASS.dto.DoctorStatsResponse;
import com.group_project.MASS.dto.PatientStatsResponse;
import com.group_project.MASS.dto.SpecialtyStatsResponse;
import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.model.DoctorProfile;
import com.group_project.MASS.model.Role;
import com.group_project.MASS.model.Specialty;
import com.group_project.MASS.repository.AppointmentRepository;
import com.group_project.MASS.repository.DoctorProfileRepository;
import com.group_project.MASS.repository.SpecialtyRepository;
import com.group_project.MASS.repository.UserRepository;
import com.group_project.MASS.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
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

    @Override
    public List<DoctorStatsResponse> getDoctorStats() {
        List<DoctorProfile> doctors = doctorProfileRepository.findAll();
        return doctors.stream().map(dp -> {
            long total = appointmentRepository.countByDoctorProfile(dp);
            long completed = appointmentRepository.countByDoctorProfileAndStatus(dp, AppointmentStatus.COMPLETED);
            long pending = appointmentRepository.countByDoctorProfileAndStatus(dp, AppointmentStatus.PENDING);
            long cancelled = appointmentRepository.countByDoctorProfileAndStatus(dp, AppointmentStatus.CANCELLED);
            return DoctorStatsResponse.builder()
                    .doctorProfileId(dp.getId())
                    .doctorName(dp.getUser().getFullName())
                    .specialtyName(dp.getSpecialty().getName())
                    .totalAppointments(total)
                    .completedAppointments(completed)
                    .pendingAppointments(pending)
                    .cancelledAppointments(cancelled)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public PatientStatsResponse getPatientStats() {
        long totalPatients = userRepository.countByRole(Role.ROLE_PATIENT);
        long totalAppointments = appointmentRepository.count();
        long completed = appointmentRepository.countByStatus(AppointmentStatus.COMPLETED);
        long pending = appointmentRepository.countByStatus(AppointmentStatus.PENDING);
        long cancelled = appointmentRepository.countByStatus(AppointmentStatus.CANCELLED);

        return PatientStatsResponse.builder()
                .totalPatients(totalPatients)
                .totalAppointments(totalAppointments)
                .completedAppointments(completed)
                .pendingAppointments(pending)
                .cancelledAppointments(cancelled)
                .build();
    }

    @Override
    public List<SpecialtyStatsResponse> getSpecialtyStats() {
        List<Specialty> specialties = specialtyRepository.findAll();
        return specialties.stream().map(s -> {
            List<DoctorProfile> doctors = doctorProfileRepository.findBySpecialtyId(s.getId());
            long totalDoctors = doctors.size();
            long totalAppointments = doctors.stream()
                    .mapToLong(appointmentRepository::countByDoctorProfile)
                    .sum();
            long completedAppointments = doctors.stream()
                    .mapToLong(dp -> appointmentRepository.countByDoctorProfileAndStatus(dp, AppointmentStatus.COMPLETED))
                    .sum();
            return SpecialtyStatsResponse.builder()
                    .specialtyId(s.getId())
                    .specialtyName(s.getName())
                    .totalDoctors(totalDoctors)
                    .totalAppointments(totalAppointments)
                    .completedAppointments(completedAppointments)
                    .build();
        }).collect(Collectors.toList());
    }
}
