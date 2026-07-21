package com.group_project.MASS.service;

import com.group_project.MASS.dto.SpecialtyRequest;
import com.group_project.MASS.dto.SpecialtyResponse;
import com.group_project.MASS.model.Specialty;
import com.group_project.MASS.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyService {

    @Autowired
    private SpecialtyRepository specialtyRepository;

    // Chuyển Specialty entity → SpecialtyResponse DTO
    private SpecialtyResponse toResponse(Specialty specialty) {
        return SpecialtyResponse.builder()
                .id(specialty.getId())
                .name(specialty.getName())
                .description(specialty.getDescription())
                .imageUrl(specialty.getImageUrl())
                .build();
    }

    // Lấy tất cả chuyên khoa
    public List<SpecialtyResponse> getAllSpecialties() {
        return specialtyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Tạo mới chuyên khoa
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

    // Cập nhật chuyên khoa
    public SpecialtyResponse updateSpecialty(Long id, SpecialtyRequest request) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa với id: " + id));
        specialty.setName(request.getName());
        specialty.setDescription(request.getDescription());
        specialty.setImageUrl(request.getImageUrl());
        return toResponse(specialtyRepository.save(specialty));
    }

    // Xóa chuyên khoa
    public void deleteSpecialty(Long id) {
        if (!specialtyRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy chuyên khoa với id: " + id);
        }
        specialtyRepository.deleteById(id);
    }
}
