package com.group_project.MASS.controller;

import com.group_project.MASS.dto.request.MedicalRecordRequest;
import com.group_project.MASS.dto.response.MedicalRecordResponse;
import com.group_project.MASS.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
@PreAuthorize("hasAuthority('ROLE_DOCTOR')")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    // GET /api/medical-records/appointment/{appointmentId}
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<MedicalRecordResponse> getByAppointmentId(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(medicalRecordService.getByAppointmentId(appointmentId));
    }

    // POST /api/medical-records — Tạo hồ sơ y tế mới
    @PostMapping
    public ResponseEntity<MedicalRecordResponse> createMedicalRecord(
            @Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(medicalRecordService.createMedicalRecord(request));
    }

    // PUT /api/medical-records/{id} — Cập nhật hồ sơ y tế
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponse> updateMedicalRecord(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(id, request));
    }
}
