package com.group_project.MASS.service;

import com.group_project.MASS.dto.DoctorDto;

import java.util.List;

public interface DoctorService {
    List<DoctorDto> getDoctors(Long specialtyId, String name);
    DoctorDto getDoctorById(Long id);
}
