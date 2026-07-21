package com.group_project.MASS.service;

import com.group_project.MASS.dto.DoctorRequest;
import com.group_project.MASS.dto.DoctorResponse;

import java.util.List;

public interface DoctorProfileService {
    List<DoctorResponse> getAllDoctors();
    DoctorResponse createDoctor(DoctorRequest request);
    DoctorResponse updateDoctor(Long id, DoctorRequest request);
    void deleteDoctor(Long id);
}
