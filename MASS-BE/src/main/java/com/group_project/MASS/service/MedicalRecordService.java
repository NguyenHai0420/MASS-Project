package com.group_project.MASS.service;

import com.group_project.MASS.dto.request.MedicalRecordRequest;
import com.group_project.MASS.dto.response.MedicalRecordResponse;

public interface MedicalRecordService {
    MedicalRecordResponse getByAppointmentId(Long appointmentId);
    MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request);
    MedicalRecordResponse updateMedicalRecord(Long id, MedicalRecordRequest request);
}
