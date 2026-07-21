package com.group_project.MASS.service.impl;

import com.group_project.MASS.dto.SpecialtyRequest;
import com.group_project.MASS.dto.SpecialtyResponse;
import com.group_project.MASS.model.Specialty;
import com.group_project.MASS.repository.SpecialtyRepository;
import com.group_project.MASS.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    @Autowired
    private SpecialtyRepository specialtyRepository;

    private SpecialtyResponse toResponse(Specialty specialty) {
        return SpecialtyResponse.builder()
                .id(specialty.getId())
                .name(specialty.getName())
                .description(specialty.getDescription())
                .imageUrl(specialty.getImageUrl())
                .build();
    }

    @Override
    public List<SpecialtyResponse> getAllSpecialties() {
        return specialtyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SpecialtyResponse createSpecialty(SpecialtyRequest request) {
        if (specialtyRepository.existsByName(request.getName())) {
            throw new RuntimeException("Chuyên khoa '" + request.getName() + "' đã tồn tại");
        }
        Specialty specialty = Specialty.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();
        return toResponse(specialtyRepository.save(specialty));
    }

    @Override
    public SpecialtyResponse updateSpecialty(Long id, SpecialtyRequest request) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa với id: " + id));
        specialty.setName(request.getName());
        specialty.setDescription(request.getDescription());
        specialty.setImageUrl(request.getImageUrl());
        return toResponse(specialtyRepository.save(specialty));
    }

    @Override
    public void deleteSpecialty(Long id) {
        if (!specialtyRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy chuyên khoa với id: " + id);
        }
        specialtyRepository.deleteById(id);
    }
}
