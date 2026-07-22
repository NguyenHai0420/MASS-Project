package com.group_project.MASS.controller;

import com.group_project.MASS.dto.request.ClinicInformationRequest;
import com.group_project.MASS.dto.response.ClinicInformationResponse;
import com.group_project.MASS.service.ClinicInformationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/clinic")
public class ClinicInformationController {

    @Autowired
    private ClinicInformationService clinicInformationService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ClinicInformationResponse>> getAllClinicInformation() {
        return ResponseEntity.ok(clinicInformationService.getAllClinicInformation());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClinicInformationResponse> createClinicInformation(
            @Valid @RequestBody ClinicInformationRequest request) {
        return ResponseEntity.ok(clinicInformationService.createClinicInformation(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClinicInformationResponse> updateClinicInformation(
            @PathVariable Long id,
            @Valid @RequestBody ClinicInformationRequest request) {
        return ResponseEntity.ok(clinicInformationService.updateClinicInformation(id, request));
    }
}
