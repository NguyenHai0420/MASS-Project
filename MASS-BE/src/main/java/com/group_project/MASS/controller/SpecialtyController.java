package com.group_project.MASS.controller;

import com.group_project.MASS.dto.SpecialtyDto;
import com.group_project.MASS.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<SpecialtyDto>> getAllSpecialties() {
        return ResponseEntity.ok(specialtyService.getAllSpecialties());
    }
}
