package com.group_project.MASS.service;

import com.group_project.MASS.dto.ScheduleDto;
import com.group_project.MASS.dto.request.ScheduleRequest;
import com.group_project.MASS.dto.response.ScheduleResponse;

import java.util.List;

public interface ScheduleService {
    List<ScheduleResponse> getMySchedules(String email);
    ScheduleResponse createSchedule(String email, ScheduleRequest request);
    void deleteSchedule(Long id, String email);
    List<ScheduleDto> getDoctorSchedules(Long doctorId, String date);
}
