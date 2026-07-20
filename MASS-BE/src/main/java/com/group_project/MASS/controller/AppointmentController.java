package com.group_project.MASS.controller;

import com.group_project.MASS.dto.request.CancelAppointmentRequest;
import com.group_project.MASS.dto.request.CreateWalkInAppointmentRequest;
import com.group_project.MASS.dto.request.UpdateAppointmentRequest;
import com.group_project.MASS.dto.request.UpdateAppointmentStatusRequest;
import com.group_project.MASS.dto.response.ApiMessageResponse;
import com.group_project.MASS.dto.response.AppointmentDetailResponse;
import com.group_project.MASS.dto.response.AppointmentListResponse;
import com.group_project.MASS.dto.response.AvailableScheduleResponse;
import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/receptionist/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentListResponse>> getAppointments(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(required = false)
            Long specialtyId,
            @RequestParam(required = false)
            AppointmentStatus status
    ) {
        return ResponseEntity
                .ok(appointmentService.getAppointments(date, specialtyId, status));
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDetailResponse> getAppointmentDetail(
            @PathVariable Long appointmentId
    ) {
        return ResponseEntity
                .ok(appointmentService.getAppointmentDetail(appointmentId));
    }

    @GetMapping("/available-sheduels")
    public ResponseEntity<List<AvailableScheduleResponse>> getAvailableSheduels(
            @RequestParam(required = false)
            Long specialtyId,
            @RequestParam(required = false)
            Long doctorProfileId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalTime fromTime
    ) {
        return ResponseEntity
                .ok(appointmentService.getAvailableSchedules(specialtyId, doctorProfileId, date, fromTime));
    }

    @PostMapping("/walk-in")
    public ResponseEntity<AppointmentDetailResponse> createWalkInAppointment(
            @Valid
            @RequestBody
            CreateWalkInAppointmentRequest request
    ) {
        return ResponseEntity
                .ok(appointmentService.createWalkInAppointment(request));
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDetailResponse> updateAppointment(
            @PathVariable
            Long appointmentId,
            @Valid
            @RequestBody
            UpdateAppointmentRequest request
    ) {
        return ResponseEntity
                .ok(appointmentService.updateAppointment(appointmentId, request));
    }

    @PatchMapping("/{appointmentId}/status")
    public ResponseEntity<ApiMessageResponse> updateAppointmentStatus(
            @PathVariable
            Long appointmentId,
            @Valid
            @RequestBody
            UpdateAppointmentStatusRequest request
    ) {
        return ResponseEntity
                .ok(appointmentService.updateAppointmentStatus(appointmentId, request));
    }

    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<ApiMessageResponse> cancelAppointment(
            @PathVariable
            Long appointmentId,
            @Valid
            @RequestBody
            CancelAppointmentRequest request
    ) {
        return ResponseEntity
                .ok(appointmentService.cancelAppointment(appointmentId, request));
    }

}
