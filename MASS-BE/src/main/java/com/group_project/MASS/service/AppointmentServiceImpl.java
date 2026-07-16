package com.group_project.MASS.service;

import com.group_project.MASS.dto.request.CancelAppointmentRequest;
import com.group_project.MASS.dto.request.CreateWalkInAppointmentRequest;
import com.group_project.MASS.dto.request.UpdateAppointmentRequest;
import com.group_project.MASS.dto.request.UpdateAppointmentStatusRequest;
import com.group_project.MASS.dto.response.ApiMessageResponse;
import com.group_project.MASS.dto.response.AppointmentDetailResponse;
import com.group_project.MASS.dto.response.AppointmentListResponse;
import com.group_project.MASS.dto.response.AvailableScheduleResponse;
import com.group_project.MASS.mapper.AppointmentMapper;
import com.group_project.MASS.model.Appointment;
import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.model.Payment;
import com.group_project.MASS.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final PaymentRepository paymentRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final SpecialtyRepository specialtyRepository;
    private final UserRepository userRepository;


    // Appointment List
    // /api/receptionist/appointments
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentListResponse> getAppointments(LocalDate date,
                                                         Long specialtyId,
                                                         AppointmentStatus status) {

        LocalDate selectedDate = date != null ? date : LocalDate.now();

        List<Appointment> appointments;

        if (specialtyId != null && status != null) {
            // filter appointments based on specialtyId and status
            appointments = appointmentRepository
                    .findByDoctorProfileSpecialtyIdAndScheduleDateAndStatusOrderByScheduleStartTimeAsc(specialtyId, selectedDate, status);

        } else if (specialtyId != null) {
            // filter appointments based on specialtyId only
            appointments = appointmentRepository
                    .findByDoctorProfileSpecialtyIdAndScheduleDateOrderByScheduleStartTimeAsc(specialtyId, selectedDate);
        } else if (status != null) {
            // filter appointments based on status only
            appointments = appointmentRepository
                    .findByScheduleDateAndStatusOrderByScheduleStartTimeAsc(selectedDate, status);
        } else {
            // no filters, get all appointments for the selected date
            appointments = appointmentRepository
                    .findByScheduleDateOrderByScheduleStartTimeAsc(selectedDate);
        }

        return appointments.stream()
                .map(this::mapToListResponse)
                .toList();
    }

    // Appointment Detail
    @Override
    @Transactional(readOnly = true)
    public AppointmentDetailResponse getAppointmentDetail(Long appointmentId) {

        if (appointmentId == null) {
            throw new RuntimeException("ID của lịch hẹn không được để trống");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc hẹn với ID: " + appointmentId));

        Payment payment = paymentRepository
                .findByAppointmentId(appointmentId)
                .orElse(null);

        return AppointmentMapper.toDetailResponse(
                appointment,
                payment
        );
    }

    private AppointmentListResponse mapToListResponse(Appointment appointment) {
        Payment payment = paymentRepository
                .findByAppointmentId(appointment.getId())
                .orElse(null);

        return AppointmentMapper.toListResponse(
                appointment,
                payment
        );
    }

    @Override
    public AppointmentDetailResponse createWalkInAppointment(
            CreateWalkInAppointmentRequest request
    ) {
        throw new UnsupportedOperationException(
                "Chức năng tạo lịch walk-in chưa được triển khai"
        );
    }

    @Override
    public AppointmentDetailResponse updateAppointment(
            Long appointmentId,
            UpdateAppointmentRequest request
    ) {
        throw new UnsupportedOperationException(
                "Chức năng cập nhật lịch chưa được triển khai"
        );
    }

    @Override
    public ApiMessageResponse updateAppointmentStatus(
            Long appointmentId,
            UpdateAppointmentStatusRequest request
    ) {
        throw new UnsupportedOperationException(
                "Chức năng cập nhật trạng thái chưa được triển khai"
        );
    }

    @Override
    public ApiMessageResponse cancelAppointment(
            Long appointmentId,
            CancelAppointmentRequest request
    ) {
        throw new UnsupportedOperationException(
                "Chức năng hủy lịch chưa được triển khai"
        );
    }

    @Override
    public List<AvailableScheduleResponse> getAvailableSchedules(
            Long specialtyId,
            Long doctorProfileId,
            LocalDate date,
            LocalTime fromTime
    ) {
        throw new UnsupportedOperationException(
                "Chức năng lấy slot trống chưa được triển khai"
        );
    }
}
