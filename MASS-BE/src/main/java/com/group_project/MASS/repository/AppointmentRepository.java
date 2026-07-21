package com.group_project.MASS.repository;

import com.group_project.MASS.model.Appointment;
import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.model.AppointmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    // Find all appointments for a specific date, ordered
    Page<Appointment> findByScheduleDate(LocalDate date, Pageable page);

    // Find all appointments for a specific date and status, ordered
    Page<Appointment> findByScheduleDateAndStatus(LocalDate date, AppointmentStatus  status, Pageable page);

    // Find all appointments for a specific doctor profile and date, ordered
    Page<Appointment> findByDoctorProfileSpecialtyIdAndScheduleDate(Long specialtyId, LocalDate date, Pageable page);

    // Find all appointments for a specific doctor profile, date, and status, ordered
    Page<Appointment> findByDoctorProfileSpecialtyIdAndScheduleDateAndStatus(
            Long specialtyId,
            LocalDate date,
            AppointmentStatus status,
            Pageable page
    );

    // Find all appointments by status
    Page<Appointment> findByStatus(AppointmentStatus status, Pageable pageable);

    Page<Appointment> findByDoctorProfileSpecialtyId(
            Long specialtyId,
            Pageable pageable
    );

    Page<Appointment> findByDoctorProfileSpecialtyIdAndStatus(Long specialtyId, AppointmentStatus status, Pageable pageable
    );

    // Find all appointments for a specific doctor profile and date, ordered
    Page<Appointment> findByDoctorProfileIdAndScheduleDate(Long doctorProfileId, LocalDate date, Pageable page);

    // Find all appointments for a specific date and type, ordered
    List<Appointment> findByScheduleDateAndType(LocalDate date, AppointmentType type);

    // Check if an appointment exists for a specific schedule and status not equal to the given status
    boolean existsByScheduleIdAndStatusNot(Long scheduleId, AppointmentStatus status);

}
