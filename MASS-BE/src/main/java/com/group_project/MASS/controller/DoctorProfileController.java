package com.group_project.MASS.controller;

import com.group_project.MASS.dto.DoctorRequest;
import com.group_project.MASS.dto.DoctorResponse;
import com.group_project.MASS.service.DoctorProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/doctors")
public class DoctorProfileController {

    @Autowired
    private DoctorProfileService doctorProfileService;

    // GET /api/doctors — Ai cũng xem được
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorProfileService.getAllDoctors());
    }

    // POST /api/doctors — Chỉ ADMIN
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorProfileService.createDoctor(request));
    }

    // PUT /api/doctors/{id} — Chỉ ADMIN (id là DoctorProfile id)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorProfileService.updateDoctor(id, request));
    }

    // DELETE /api/doctors/{id} — Chỉ ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        doctorProfileService.deleteDoctor(id);
        return ResponseEntity.ok().build();
    }
}
