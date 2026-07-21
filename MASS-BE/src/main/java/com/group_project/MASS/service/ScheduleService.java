package com.group_project.MASS.service;

import com.group_project.MASS.dto.ScheduleDto;
import java.util.List;

public interface ScheduleService {
    List<ScheduleDto> getDoctorSchedules(Long doctorId, String date);
}
