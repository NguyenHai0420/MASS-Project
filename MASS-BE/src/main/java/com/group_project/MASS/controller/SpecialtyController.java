package com.group_project.MASS.controller;

import com.group_project.MASS.dto.SpecialtyRequest;
import com.group_project.MASS.dto.SpecialtyResponse;
import com.group_project.MASS.service.SpecialtyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;

    // GET /api/specialties — Ai cũng xem được (dùng cho patient chọn specialty)
    @GetMapping
    public ResponseEntity<List<SpecialtyResponse>> getAllSpecialties() {
        return ResponseEntity.ok(specialtyService.getAllSpecialties());
    }

    // POST /api/specialties — Chỉ ADMIN
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<SpecialtyResponse> createSpecialty(@Valid @RequestBody SpecialtyRequest request) {
        return ResponseEntity.ok(specialtyService.createSpecialty(request));
    }

    // PUT /api/specialties/{id} — Chỉ ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<SpecialtyResponse> updateSpecialty(
            @PathVariable Long id,
            @Valid @RequestBody SpecialtyRequest request) {
        return ResponseEntity.ok(specialtyService.updateSpecialty(id, request));
    }

    // DELETE /api/specialties/{id} — Chỉ ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteSpecialty(@PathVariable Long id) {
        specialtyService.deleteSpecialty(id);
        return ResponseEntity.ok().build();
    }
}
