package com.group_project.MASS.repository;

import com.group_project.MASS.model.Appointment;
import com.group_project.MASS.model.AppointmentStatus;
import com.group_project.MASS.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Lấy tất cả appointment của một doctor
    List<Appointment> findByDoctorProfileOrderByCreatedAtDesc(DoctorProfile doctorProfile);

    // Đếm theo status (dùng cho thống kê)
    long countByStatus(AppointmentStatus status);

    // Đếm theo doctor (dùng cho dashboard)
    long countByDoctorProfile(DoctorProfile doctorProfile);

    // Đếm tổng bệnh nhân (distinct)
    @Query("SELECT COUNT(DISTINCT a.patient.id) FROM Appointment a")
    long countDistinctPatients();
}
