package com.group_project.MASS.service;

import com.group_project.MASS.dto.ClinicInformationRequest;
import com.group_project.MASS.dto.ClinicInformationResponse;
import com.group_project.MASS.model.ClinicInformation;
import com.group_project.MASS.repository.ClinicInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClinicInformationService {

    @Autowired
    private ClinicInformationRepository clinicInformationRepository;

    // Chuyển ClinicInformation entity → ClinicInformationResponse DTO
    private ClinicInformationResponse toResponse(ClinicInformation clinic) {
        return ClinicInformationResponse.builder()
                .id(clinic.getId())
                .name(clinic.getName())
                .address(clinic.getAddress())
                .phone(clinic.getPhone())
                .email(clinic.getEmail())
                .workingHours(clinic.getWorkingHours())
                .build();
    }

    // Lấy thông tin phòng khám (lấy bản ghi đầu tiên)
    public ClinicInformationResponse getClinicInformation() {
        return clinicInformationRepository.findAll()
                .stream()
                .findFirst()
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Chưa có thông tin phòng khám"));
    }

    // Lấy tất cả (cho Admin quản lý)
    public List<ClinicInformationResponse> getAllClinicInformation() {
        return clinicInformationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Tạo mới thông tin phòng khám
    public ClinicInformationResponse createClinicInformation(ClinicInformationRequest request) {
        ClinicInformation clinic = ClinicInformation.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .workingHours(request.getWorkingHours())
                .build();
        return toResponse(clinicInformationRepository.save(clinic));
    }

    // Cập nhật thông tin phòng khám
    public ClinicInformationResponse updateClinicInformation(Long id, ClinicInformationRequest request) {
        ClinicInformation clinic = clinicInformationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin phòng khám với id: " + id));
        clinic.setName(request.getName());
        clinic.setAddress(request.getAddress());
        clinic.setPhone(request.getPhone());
        clinic.setEmail(request.getEmail());
        clinic.setWorkingHours(request.getWorkingHours());
        return toResponse(clinicInformationRepository.save(clinic));
    }
}
