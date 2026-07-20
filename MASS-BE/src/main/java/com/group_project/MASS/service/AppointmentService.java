package com.group_project.MASS.service;

<<<<<<< HEAD
import com.group_project.MASS.dto.AppointmentResponse;
import com.group_project.MASS.dto.request.CancelAppointmentRequest;
import com.group_project.MASS.dto.request.CreateWalkInAppointmentRequest;
import com.group_project.MASS.dto.request.UpdateAppointmentRequest;
import com.group_project.MASS.dto.request.UpdateAppointmentStatusRequest;
import com.group_project.MASS.dto.response.ApiMessageResponse;
import com.group_project.MASS.dto.response.AppointmentDetailResponse;
import com.group_project.MASS.dto.response.AppointmentListResponse;
import com.group_project.MASS.dto.response.AvailableScheduleResponse;
import com.group_project.MASS.model.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {
    // Lấy tất cả appointment của doctor đang đăng nhập
    List<AppointmentResponse> getMyAppointments(String email);

    // Lấy danh sách các cuộc hẹn dựa trên ngày, chuyên khoa và trạng thái
    List<AppointmentListResponse> getAppointments(LocalDate date, Long specialtyId, AppointmentStatus status);

    // Lấy chi tiết của cuộc hẹn dựa trên ID
    AppointmentDetailResponse getAppointmentDetail(Long appointmentId);

    // Tạo một lịch hẹn mới trực tiếp tại phòng khám dựa trên dữ liệu yêu cầu được cung cấp
    AppointmentDetailResponse createWalkInAppointment(CreateWalkInAppointmentRequest createWalkInAppointmentRequest);

    // Cập nhật thông tin của một cuộc hẹn hiện có dựa trên ID của cuộc hẹn và dữ liệu yêu cầu được cung cấp
    AppointmentDetailResponse updateAppointment(Long appointmentId, UpdateAppointmentRequest updateAppointmentRequest);

    // Cập nhật trạng thái của một cuộc hẹn hiện có dựa trên ID của cuộc hẹn và dữ liệu yêu cầu được cung cấp
    ApiMessageResponse updateAppointmentStatus(Long appointmentId, UpdateAppointmentStatusRequest updateAppointmentStatusRequest);

    // Hủy một cuộc hẹn hiện có dựa trên ID của cuộc hẹn và dữ liệu yêu cầu được cung cấp
    ApiMessageResponse cancelAppointment(Long appointmentId, CancelAppointmentRequest cancelAppointmentRequest);

    // Lấy danh sách các lịch trình có sẵn dựa trên chuyên khoa và ngày
    List<AvailableScheduleResponse> getAvailableSchedules(Long specialtyId, Long doctorProfileId, LocalDate date, LocalTime fromTime);
=======
import com.group_project.MASS.dto.AppointmentDto;
import com.group_project.MASS.dto.AppointmentRequestDto;
import com.group_project.MASS.dto.RescheduleRequestDto;
import com.group_project.MASS.model.*;
import com.group_project.MASS.repository.AppointmentRepository;
import com.group_project.MASS.repository.DoctorProfileRepository;
import com.group_project.MASS.repository.ScheduleRepository;
import com.group_project.MASS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository; 

    public AppointmentDto bookAppointment(AppointmentRequestDto request, String patientEmail) {
        // Fetch patient
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.isAvailable()) {
            throw new RuntimeException("Schedule is no longer available");
        }

        // Create appointment
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

        // Mark schedule as unavailable
        schedule.setAvailable(false);
        scheduleRepository.save(schedule);

        return mapToDto(appointment);
    }

    public List<AppointmentDto> getMyAppointments(String patientEmail) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return appointmentRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId())
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public AppointmentDto cancelAppointment(Long appointmentId, String patientEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getPatient().getEmail().equals(patientEmail)) {
            throw new RuntimeException("Not authorized to cancel this appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment = appointmentRepository.save(appointment);

        // Free up schedule
        Schedule schedule = appointment.getSchedule();
        schedule.setAvailable(true);
        scheduleRepository.save(schedule);

        return mapToDto(appointment);
    }

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

        // Update schedule
        appointment.setSchedule(newSchedule);
        appointment = appointmentRepository.save(appointment);

        // Free old schedule
        oldSchedule.setAvailable(true);
        scheduleRepository.save(oldSchedule);
        
        // Take new schedule
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
>>>>>>> origin/uyenht
}
