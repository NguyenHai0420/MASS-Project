package com.group_project.MASS.service;

import com.group_project.MASS.dto.request.ClinicInformationRequest;
import com.group_project.MASS.dto.ClinicInformationResponse;

import java.util.List;

public interface ClinicInformationService {
    ClinicInformationResponse getClinicInformation();
    List<ClinicInformationResponse> getAllClinicInformation();
    ClinicInformationResponse createClinicInformation(ClinicInformationRequest request);
    ClinicInformationResponse updateClinicInformation(Long id, ClinicInformationRequest request);
}
