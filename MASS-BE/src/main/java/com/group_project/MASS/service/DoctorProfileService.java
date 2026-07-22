package com.group_project.MASS.service;

import com.group_project.MASS.dto.request.DoctorRequest;
import com.group_project.MASS.dto.response.DoctorProfileResponse;

import java.util.List;

public interface DoctorProfileService {
    List<DoctorProfileResponse> getAllDoctors();
    DoctorProfileResponse createDoctor(DoctorRequest request);
    DoctorProfileResponse updateDoctor(Long id, DoctorRequest request);
    void deleteDoctor(Long id);
}
