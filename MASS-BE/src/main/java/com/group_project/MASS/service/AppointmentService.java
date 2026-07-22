package com.group_project.MASS.service;

import com.group_project.MASS.dto.response.AppointmentResponse;
import com.group_project.MASS.dto.request.CancelAppointmentRequest;
import com.group_project.MASS.dto.request.CreateWalkInAppointmentRequest;
import com.group_project.MASS.dto.request.UpdateAppointmentRequest;
import com.group_project.MASS.dto.request.UpdateAppointmentStatusRequest;
import com.group_project.MASS.dto.response.*;
import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.model.Specialty;
import com.group_project.MASS.dto.AppointmentDto;
import com.group_project.MASS.dto.request.AppointmentRequestDto;
import com.group_project.MASS.dto.request.RescheduleRequestDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {
    List<Specialty> getAllSpecialties();
    List<DoctorResponse> getDoctorsBySpecialty(Long specialtyId);
    // Lấy tất cả appointment của doctor đang đăng nhập
    List<AppointmentResponse> getMyAppointments(String email);
    // Lấy danh sách các cuộc hẹn dựa trên ngày, chuyên khoa và trạng thái
    PageResponse<AppointmentListResponse> getAppointments(LocalDate date, Long specialtyId, AppointmentStatus status, int page, int size);

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
    
    AppointmentDetailResponse checkInAppointment(Long appointmentId);

    AppointmentDto bookAppointment(AppointmentRequestDto request, String patientEmail);
    
    List<AppointmentDto> getPatientAppointments(String patientEmail);
    
    AppointmentDto cancelPatientAppointment(Long appointmentId, String patientEmail);
    
    AppointmentDto rescheduleAppointment(Long appointmentId, RescheduleRequestDto request, String patientEmail);
}
