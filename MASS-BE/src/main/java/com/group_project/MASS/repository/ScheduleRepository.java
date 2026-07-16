package com.group_project.MASS.repository;

import com.group_project.MASS.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    // Find all schedules for a specific doctor profile on a given date, ordered by start time in ascending order
    List<Schedule> findByDoctorProfileIdAndDateOrderByStartTimeAsc(Long doctorProfileId, LocalDate date);

    // Find all available schedules for a specific specialty on a given date and time, ordered by start time in ascending order
    List<Schedule> findByDoctorProfileSpecialtyIdAndDateStartTimeGreaterThanEqualAndIsAvailableTrueOrderByStartTimeAsc(Long specialtyId, LocalDate date, LocalTime startTime);

    // Find all available schedules for a specific doctor profile on a given date, ordered by start time in ascending order
    List<Schedule> findByDoctorProfileIdAndDateAndIsAvailableTrueOrderByStartTimeAsc(Long doctorProfileId, LocalDate date);

    // Check if a schedule exists for a specific doctor profile on a given date and start time
    boolean existsByDoctorProfileIdAndDateAndStartTime(Long doctorProfileId, LocalDate date, LocalTime startTime);
}
