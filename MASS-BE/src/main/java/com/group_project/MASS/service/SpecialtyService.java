package com.group_project.MASS.service;

import com.group_project.MASS.dto.request.SpecialtyRequest;
import com.group_project.MASS.dto.SpecialtyResponse;

import java.util.List;

public interface SpecialtyService {
    List<SpecialtyResponse> getAllSpecialties();
    SpecialtyResponse createSpecialty(SpecialtyRequest request);
    SpecialtyResponse updateSpecialty(Long id, SpecialtyRequest request);
    void deleteSpecialty(Long id);
}
