package com.group_project.MASS.service;

import com.group_project.MASS.dto.DoctorDto;
import com.group_project.MASS.model.DoctorProfile;
import com.group_project.MASS.repository.DoctorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    public List<DoctorDto> getDoctors(Long specialtyId, String name) {
        List<DoctorProfile> profiles;
        if (specialtyId != null && name != null && !name.trim().isEmpty()) {
            profiles = doctorProfileRepository.findBySpecialtyIdAndUserFullNameContainingIgnoreCase(specialtyId, name);
        } else if (specialtyId != null) {
            profiles = doctorProfileRepository.findBySpecialtyId(specialtyId);
        } else if (name != null && !name.trim().isEmpty()) {
            profiles = doctorProfileRepository.findByUserFullNameContainingIgnoreCase(name);
        } else {
            profiles = doctorProfileRepository.findAll();
        }

        return profiles.stream()
            .filter(p -> p.getUser() != null && Boolean.TRUE.equals(p.getUser().getActive()))
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public DoctorDto getDoctorById(Long id) {
        Optional<DoctorProfile> profileOpt = doctorProfileRepository.findById(id);
        return profileOpt
            .filter(profile -> profile.getUser() != null && Boolean.TRUE.equals(profile.getUser().getActive()))
            .map(this::mapToDto)
            .orElse(null);
    }

    private DoctorDto mapToDto(DoctorProfile profile) {
        return DoctorDto.builder()
            .id(profile.getId())
            .name(profile.getUser() != null ? profile.getUser().getFullName() : "Unknown")
            .specialtyName(profile.getSpecialty() != null ? profile.getSpecialty().getName() : "Unknown")
            .clinicName("Phòng khám MASS") // Mocked as per requirement
            .build();
    }
}
