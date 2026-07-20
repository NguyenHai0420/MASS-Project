package com.group_project.MASS.repository;

import com.group_project.MASS.model.DoctorProfile;
import com.group_project.MASS.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDoctorProfileOrderByDateAscStartTimeAsc(DoctorProfile doctorProfile);
}
