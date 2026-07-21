package com.group_project.MASS.service.impl;

import com.group_project.MASS.dto.AppointmentDto;
import com.group_project.MASS.dto.AppointmentRequestDto;
import com.group_project.MASS.dto.RescheduleRequestDto;
import com.group_project.MASS.model.*;
import com.group_project.MASS.repository.AppointmentRepository;
import com.group_project.MASS.repository.DoctorProfileRepository;
import com.group_project.MASS.repository.ScheduleRepository;
import com.group_project.MASS.repository.UserRepository;
import com.group_project.MASS.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository; 

    @Override
    public AppointmentDto bookAppointment(AppointmentRequestDto request, String patientEmail) {
       
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.isAvailable()) {
            throw new RuntimeException("Schedule is no longer available");
        }

      
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctorProfile(doctor)
                .schedule(schedule)
                .status(AppointmentStatus.PENDING)
                .reason(request.getReason())
                .type(AppointmentType.WALK_IN)
                .createdAt(LocalDateTime.now())
                .build();

        appointment = appointmentRepository.save(appointment);

      
        schedule.setAvailable(false);
        scheduleRepository.save(schedule);

        return mapToDto(appointment);
    }

    @Override
    public List<AppointmentDto> getMyAppointments(String patientEmail) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return appointmentRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId())
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public AppointmentDto cancelAppointment(Long appointmentId, String patientEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getPatient().getEmail().equals(patientEmail)) {
            throw new RuntimeException("Not authorized to cancel this appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment = appointmentRepository.save(appointment);

        
        Schedule schedule = appointment.getSchedule();
        schedule.setAvailable(true);
        scheduleRepository.save(schedule);

        return mapToDto(appointment);
    }

    @Override
    public AppointmentDto rescheduleAppointment(Long appointmentId, RescheduleRequestDto request, String patientEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getPatient().getEmail().equals(patientEmail)) {
            throw new RuntimeException("Not authorized to reschedule this appointment");
        }

        Schedule oldSchedule = appointment.getSchedule();
        
        Schedule newSchedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("New schedule not found"));

        if (!newSchedule.isAvailable()) {
            throw new RuntimeException("New schedule is not available");
        }

        
        appointment.setSchedule(newSchedule);
        appointment = appointmentRepository.save(appointment);

       
        oldSchedule.setAvailable(true);
        scheduleRepository.save(oldSchedule);
        
        
        newSchedule.setAvailable(false);
        scheduleRepository.save(newSchedule);

        return mapToDto(appointment);
    }

    private AppointmentDto mapToDto(Appointment a) {
        String doctorName = a.getDoctorProfile() != null && a.getDoctorProfile().getUser() != null ? 
            a.getDoctorProfile().getUser().getFullName() : "Unknown";
        String specialtyName = a.getDoctorProfile() != null && a.getDoctorProfile().getSpecialty() != null ? 
            a.getDoctorProfile().getSpecialty().getName() : "Unknown";
        Long doctorId = a.getDoctorProfile() != null ? a.getDoctorProfile().getId() : null;
        
        return AppointmentDto.builder()
                .id(a.getId())
                .doctorId(doctorId)
                .doctorName(doctorName)
                .date(a.getSchedule() != null ? a.getSchedule().getDate().toString() : "")
                .time(a.getSchedule() != null ? a.getSchedule().getStartTime() + " - " + a.getSchedule().getEndTime() : "")
                .specialty(specialtyName)
                .status(a.getStatus().name())
                .reason(a.getReason())
                .build();
    }
}
