package com.group_project.MASS.repository;

import com.group_project.MASS.model.Appointment;
import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.model.AppointmentType;
import com.group_project.MASS.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Lấy tất cả appointment của một doctor
    List<Appointment> findByDoctorProfileOrderByCreatedAtDesc(DoctorProfile doctorProfile);

    // Lấy tất cả appointment của một patient
    List<Appointment> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    // Đếm theo status (dùng cho thống kê)
    long countByStatus(AppointmentStatus status);

    // Đếm theo doctor (dùng cho dashboard)
    long countByDoctorProfile(DoctorProfile doctorProfile);

    // Đếm tổng bệnh nhân (distinct)
    @Query("SELECT COUNT(DISTINCT a.patient.id) FROM Appointment a")
    long countDistinctPatients();

    // Find all appointments for a specific date, ordered by start time in ascending order
    List<Appointment> findByScheduleDateOrderByScheduleStartTimeAsc(LocalDate date);

    // Find all appointments for a specific date and status, ordered by start time in ascending order
    List<Appointment> findByScheduleDateAndStatusOrderByScheduleStartTimeAsc(LocalDate date, AppointmentStatus status);

    // Find all appointments for a specific doctor profile and date, ordered by start time in ascending order
    List<Appointment> findByDoctorProfileSpecialtyIdAndScheduleDateOrderByScheduleStartTimeAsc(Long specialtyId, LocalDate date);

    // Find all appointments for a specific doctor profile, date, and status, ordered by start time in ascending order
    List<Appointment> findByDoctorProfileSpecialtyIdAndScheduleDateAndStatusOrderByScheduleStartTimeAsc(
            Long specialtyId,
            LocalDate date,
            AppointmentStatus status
    );

    // Find all appointments for a specific doctor profile and date, ordered by start time in ascending order
    List<Appointment> findByDoctorProfileIdAndScheduleDateOrderByScheduleStartTimeAsc(Long doctorProfileId, LocalDate date);

    // Find all appointments for a specific date and type, ordered by start time in ascending order
    List<Appointment> findByScheduleDateAndTypeOrderByScheduleStartTimeAsc(LocalDate date, AppointmentType type);

    // Check if an appointment exists for a specific schedule and status not equal to the given status
    boolean existsByScheduleIdAndStatusNot(Long scheduleId, AppointmentStatus status);
}
