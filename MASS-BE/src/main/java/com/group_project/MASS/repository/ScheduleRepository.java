package com.group_project.MASS.repository;

import com.group_project.MASS.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    // Lấy toàn bộ lịch của một bác sĩ trong ngày.
    List<Schedule> findByDoctorProfileIdAndDateOrderByStartTimeAsc(
            Long doctorProfileId,
            LocalDate date
    );

    // Tìm các slot còn trống của toàn bộ bác sĩ thuộc chuyên khoa.
    List<Schedule>
    findByDoctorProfileSpecialtyIdAndDateAndStartTimeGreaterThanEqualAndIsAvailableTrueOrderByStartTimeAsc(
            Long specialtyId,
            LocalDate date,
            LocalTime startTime
    );

    // Tìm các slot còn trống của một bác sĩ kể từ một thời điểm.
    List<Schedule>
    findByDoctorProfileIdAndDateAndStartTimeGreaterThanEqualAndIsAvailableTrueOrderByStartTimeAsc(
            Long doctorProfileId,
            LocalDate date,
            LocalTime startTime
    );

    // Hiển thị toàn bộ slot còn trống của chuyên khoa trong ngày.
    List<Schedule>
    findByDoctorProfileSpecialtyIdAndDateAndIsAvailableTrueOrderByStartTimeAsc(
            Long specialtyId,
            LocalDate date
    );

    // Hiển thị toàn bộ slot còn trống của một bác sĩ trong ngày.
    List<Schedule>
    findByDoctorProfileIdAndDateAndIsAvailableTrueOrderByStartTimeAsc(
            Long doctorProfileId,
            LocalDate date
    );

    // Kiểm tra bác sĩ đã có slot tại thời gian này chưa.
    boolean existsByDoctorProfileIdAndDateAndStartTime(
            Long doctorProfileId,
            LocalDate date,
            LocalTime startTime
    );

    // Lấy schedule theo ID và trạng thái còn trống.
    Optional<Schedule> findByIdAndIsAvailableTrue(Long id);
}
