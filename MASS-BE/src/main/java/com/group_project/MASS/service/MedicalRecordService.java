package com.group_project.MASS.service;

import com.group_project.MASS.dto.MedicalRecordRequest;
import com.group_project.MASS.dto.MedicalRecordResponse;
import com.group_project.MASS.model.Appointment;
import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.model.MedicalRecord;
import com.group_project.MASS.repository.AppointmentRepository;
import com.group_project.MASS.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Chuyển MedicalRecord entity → MedicalRecordResponse DTO
    private MedicalRecordResponse toResponse(MedicalRecord mr) {
        return MedicalRecordResponse.builder()
                .id(mr.getId())
                .appointmentId(mr.getAppointment().getId())
                .patientName(mr.getAppointment().getPatient().getFullName())
                .diagnosis(mr.getDiagnosis())
                .notes(mr.getNotes())
                .prescription(mr.getPrescription())
                .createdAt(mr.getCreatedAt())
                .build();
    }

    // Lấy medical record theo appointmentId
    public MedicalRecordResponse getByAppointmentId(Long appointmentId) {
        MedicalRecord mr = medicalRecordRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Chưa có hồ sơ y tế cho cuộc hẹn này"));
        return toResponse(mr);
    }

    // Tạo medical record mới và cập nhật status appointment → COMPLETED
    public MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc hẹn với id: " + request.getAppointmentId()));

        // Kiểm tra đã có chưa
        if (medicalRecordRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new RuntimeException("Hồ sơ y tế cho cuộc hẹn này đã tồn tại");
        }

        MedicalRecord mr = MedicalRecord.builder()
                .appointment(appointment)
                .diagnosis(request.getDiagnosis())
                .notes(request.getNotes())
                .prescription(request.getPrescription())
                .build();

        // Cập nhật trạng thái appointment → COMPLETED
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return toResponse(medicalRecordRepository.save(mr));
    }

    // Cập nhật medical record
    public MedicalRecordResponse updateMedicalRecord(Long id, MedicalRecordRequest request) {
        MedicalRecord mr = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ y tế với id: " + id));

        mr.setDiagnosis(request.getDiagnosis());
        mr.setNotes(request.getNotes());
        mr.setPrescription(request.getPrescription());

        return toResponse(medicalRecordRepository.save(mr));
    }
}
