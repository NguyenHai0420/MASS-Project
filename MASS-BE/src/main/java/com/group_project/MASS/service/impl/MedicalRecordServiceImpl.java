package com.group_project.MASS.service.impl;

import com.group_project.MASS.dto.request.MedicalRecordRequest;
import com.group_project.MASS.dto.response.MedicalRecordResponse;
import com.group_project.MASS.model.Appointment;
import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.model.MedicalRecord;
import com.group_project.MASS.model.Payment;
import com.group_project.MASS.model.PaymentStatus;
import com.group_project.MASS.repository.AppointmentRepository;
import com.group_project.MASS.repository.MedicalRecordRepository;
import com.group_project.MASS.repository.PaymentRepository;
import com.group_project.MASS.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

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

    @Override
    public MedicalRecordResponse getByAppointmentId(Long appointmentId) {
        MedicalRecord mr = medicalRecordRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Chưa có hồ sơ y tế cho cuộc hẹn này"));
        return toResponse(mr);
    }

    @Override
    public MedicalRecordResponse createMedicalRecord(MedicalRecordRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc hẹn với id: " + request.getAppointmentId()));

        if (medicalRecordRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new RuntimeException("Hồ sơ y tế cho cuộc hẹn này đã tồn tại");
        }

        Payment payment = paymentRepository.findByAppointmentId(request.getAppointmentId())
                .orElse(null);
        if (payment == null || payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Bệnh nhân chưa thanh toán, không thể hoàn thành cuộc hẹn!");
        }

        MedicalRecord mr = MedicalRecord.builder()
                .appointment(appointment)
                .diagnosis(request.getDiagnosis())
                .notes(request.getNotes())
                .prescription(request.getPrescription())
                .build();

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return toResponse(medicalRecordRepository.save(mr));
    }

    @Override
    public MedicalRecordResponse updateMedicalRecord(Long id, MedicalRecordRequest request) {
        MedicalRecord mr = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ y tế với id: " + id));

        mr.setDiagnosis(request.getDiagnosis());
        mr.setNotes(request.getNotes());
        mr.setPrescription(request.getPrescription());

        return toResponse(medicalRecordRepository.save(mr));
    }
}
