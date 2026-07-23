package com.group_project.MASS.controller;

import com.group_project.MASS.dto.DoctorDto;
import com.group_project.MASS.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getDoctors(
            @RequestParam(required = false) Long specialtyId,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(doctorService.getDoctors(specialtyId, name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        DoctorDto dto = doctorService.getDoctorById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/profile/me")
    public ResponseEntity<DoctorDto> getMyDoctorProfile(java.security.Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String email = principal.getName();
        DoctorDto dto = doctorService.getMyDoctorProfile(email);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
}
